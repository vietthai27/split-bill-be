package com.thai27.split_bill_be.Repository;

import com.thai27.split_bill_be.DTO.BillHistoryDto;
import com.thai27.split_bill_be.Entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill,Long> {

    @Query(value = "select id as id, bill_name as billName, created_at as createdAt from bill", nativeQuery = true)
    List<BillHistoryDto> getBillHistory();

}
