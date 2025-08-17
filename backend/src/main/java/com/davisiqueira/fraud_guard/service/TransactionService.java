package com.davisiqueira.fraud_guard.service;

import com.davisiqueira.fraud_guard.dto.transaction.TransactionRequestDTO;
import com.davisiqueira.fraud_guard.dto.transaction.TransactionResponseDTO;
import com.davisiqueira.fraud_guard.mapper.TransactionMapper;
import com.davisiqueira.fraud_guard.model.TransactionModel;
import com.davisiqueira.fraud_guard.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TransactionService {
    private TransactionRepository repository;
    private TransactionMapper mapper;

    public TransactionService(TransactionRepository repository, TransactionMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public TransactionResponseDTO create(TransactionRequestDTO transactionDTO) {
        TransactionModel transaction = mapper.toModel(transactionDTO);
        transaction.setDate(Instant.now());

        if (true) {
            transaction.setSuspect(true);
            // Send async message to SQS
        }

        return mapper.toResponseDTO(repository.save(transaction));
    }
}
