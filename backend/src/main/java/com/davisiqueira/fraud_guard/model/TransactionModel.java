package com.davisiqueira.fraud_guard.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "transactions")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TransactionModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 11)
    private String cpf;

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP")
    private Instant date;

    @Column(nullable = false)
    private BigDecimal value;

    @Column(nullable = false)
    private Double lat;

    @Column(nullable = false)
    private Double lon;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean suspect = false;
}
