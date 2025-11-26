package com.thai27.split_bill_be.DTO;

import java.util.Date;

public interface BillHistoryDto {
    Long getId();
    String getBillName();
    Date getCreatedAt();
}
