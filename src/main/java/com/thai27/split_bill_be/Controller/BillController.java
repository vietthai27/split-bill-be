package com.thai27.split_bill_be.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thai27.split_bill_be.DTO.BillHistoryDto;
import com.thai27.split_bill_be.DTO.BillSummary;
import com.thai27.split_bill_be.DTO.Member;
import com.thai27.split_bill_be.Entity.Bill;
import com.thai27.split_bill_be.Service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bill")
public class BillController {

    @Autowired
    BillService billService;

    @PostMapping("/calculate")
    public ResponseEntity<BillSummary> calculateBill(@RequestBody List<Member> members, @RequestParam(name = "bill-name") String billName) throws JsonProcessingException {
        return billService.calculateBill(members, billName);
    }

    @GetMapping("/bill-history")
    public ResponseEntity<List<BillHistoryDto>> billHistory() {
        return billService.billHistory();
    }

    @GetMapping("/bill-by-id/{id}")
    public ResponseEntity<Bill> billById(@PathVariable Long id) {
        return billService.billById(id);
    }
}
