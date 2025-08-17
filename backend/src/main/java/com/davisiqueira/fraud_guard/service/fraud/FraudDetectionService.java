package com.davisiqueira.fraud_guard.service.fraud;

import com.davisiqueira.fraud_guard.model.TransactionModel;
import com.davisiqueira.fraud_guard.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class FraudDetectionService {
    private TransactionRepository repository;

    public FraudDetectionService(TransactionRepository repository) {
        this.repository = repository;
    }

    public boolean isSuspect(TransactionModel transaction) {
        return transaction.getValue().compareTo(BigDecimal.valueOf(10000)) > 0;
    }
}
