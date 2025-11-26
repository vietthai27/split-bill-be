package com.thai27.split_bill_be.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillSummary {
    private double totalAmount;
    private Map<String, Double> memberTotals;
    private List<Transaction> settlements;
}
