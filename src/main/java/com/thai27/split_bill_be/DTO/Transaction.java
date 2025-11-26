package com.thai27.split_bill_be.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private String payerName;
    private String recipientName;
    private double amount;
}
