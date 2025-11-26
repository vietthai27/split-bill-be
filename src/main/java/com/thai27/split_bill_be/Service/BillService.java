package com.thai27.split_bill_be.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thai27.split_bill_be.DTO.*;
import com.thai27.split_bill_be.Entity.Bill;
import com.thai27.split_bill_be.Repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

@Service
public class BillService {

    @Autowired
    BillRepository billRepository;

    public ResponseEntity<BillSummary> calculateBill(@RequestBody List<Member> members, String billName) throws JsonProcessingException {

        Map<String, Double> memberTotals = new HashMap<>();
        double totalAmount = 0;
        int paymentCount = 0;

        for (Member m : members) {
            double memberSum = 0;

            if (m.getPayments() != null) {
                for (Payment p : m.getPayments()) {
                    try {
                        memberSum += Double.parseDouble(p.getAmount());
                        paymentCount++;
                    } catch (Exception ignored) {
                    }
                }
            }

            memberTotals.put(m.getName(), memberSum);
            totalAmount += memberSum;
        }

// Calculate the average per member
        double averagePerMember = 0;
        if (!members.isEmpty()) {
            averagePerMember = totalAmount / members.size();
        }

// Initialize the new lists
        List<Member> highPayers = new ArrayList<>();
        List<Member> lowPayers = new ArrayList<>();

// Sort members into lists
        for (Member m : members) {
            double myTotal = memberTotals.getOrDefault(m.getName(), 0.0);

            if (myTotal > averagePerMember) {
                highPayers.add(m);
            } else {
                lowPayers.add(m);
            }
        }

// --- Settlement Calculation ---
        List<Transaction> settlementTransactions = new ArrayList<>(); // Initialize the result list

// Create tracking lists for dynamic balances
        List<Double> debts = new ArrayList<>();
        List<Double> credits = new ArrayList<>();

// Calculate initial debt/credit
        for (Member m : lowPayers) {
            double paid = memberTotals.getOrDefault(m.getName(), 0.0);
            debts.add(averagePerMember - paid);
        }
        for (Member m : highPayers) {
            double paid = memberTotals.getOrDefault(m.getName(), 0.0);
            credits.add(paid - averagePerMember);
        }

// Iterate using two pointers to match debts to credits
        int debtorIndex = 0;
        int creditorIndex = 0;

        while (debtorIndex < lowPayers.size() && creditorIndex < highPayers.size()) {
            double debtAmount = debts.get(debtorIndex);
            double creditAmount = credits.get(creditorIndex);

            double amountToPay = Math.min(debtAmount, creditAmount);

            // Round to 2 decimals for currency display
            amountToPay = Math.round(amountToPay * 100.0) / 100.0;

            if (amountToPay > 0.01) { // Check for meaningful payment
                String debtorName = lowPayers.get(debtorIndex).getName();
                String creditorName = highPayers.get(creditorIndex).getName();

                // CAPTURE THE TRANSACTION RESULT
                settlementTransactions.add(new Transaction(debtorName, creditorName, amountToPay));

                // Replaces System.out.println for logging
                // System.out.println(debtorName + " needs to pay " + amountToPay + " to " + creditorName);
            }

            // Update the remaining balances
            debts.set(debtorIndex, debtAmount - amountToPay);
            credits.set(creditorIndex, creditAmount - amountToPay);

            // Move pointers if a person is fully settled
            if (debts.get(debtorIndex) < 0.01) {
                debtorIndex++;
            }
            if (credits.get(creditorIndex) < 0.01) {
                creditorIndex++;
            }
        }

        Bill saveBill = new Bill();
        saveBill.setBillName(billName);
        saveBill.setCreatedAt(new Date());
        ObjectMapper objectMapper = new ObjectMapper();
        String membersJsonString = objectMapper.writeValueAsString(members);
        saveBill.setRequest(membersJsonString);
        BillSummary billResult = new BillSummary(totalAmount, memberTotals, settlementTransactions);
        String resultJsonString = objectMapper.writeValueAsString(billResult);
        saveBill.setResponse(resultJsonString);
        billRepository.save(saveBill);

        return ResponseEntity.ok(billResult);
    }

    public ResponseEntity<List<BillHistoryDto>> billHistory() {
        return ResponseEntity.ok(billRepository.getBillHistory());
    }

    public ResponseEntity<Bill> billById(Long id) {
        return ResponseEntity.ok(billRepository.findById(id).get());
    }

}
