package com.thai27.split_bill_be.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "bill")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Lob
    @Column(name = "request", columnDefinition = "TEXT")
    String request;

    @Lob
    @Column(name = "response", columnDefinition = "TEXT")
    String response;

    @Column(name = "bill_name")
    String billName;

    @Column(name = "created_at")
    Date createdAt;
}
