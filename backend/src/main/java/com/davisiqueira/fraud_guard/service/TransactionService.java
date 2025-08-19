package com.davisiqueira.fraud_guard.service;

import com.davisiqueira.fraud_guard.dto.transaction.TransactionRequestDTO;
import com.davisiqueira.fraud_guard.dto.transaction.TransactionResponseDTO;
import com.davisiqueira.fraud_guard.dto.transaction.TransactionsStatisticsDTO;
import com.davisiqueira.fraud_guard.mapper.TransactionMapper;
import com.davisiqueira.fraud_guard.model.TransactionModel;
import com.davisiqueira.fraud_guard.repository.TransactionRepository;
import com.davisiqueira.fraud_guard.service.fraud.FraudDetectionService;
import com.davisiqueira.fraud_guard.util.StatisticalUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    private final TransactionRepository repository;
    private final TransactionMapper mapper;
    private final FraudDetectionService fraudService;

    public TransactionService(TransactionRepository repository, TransactionMapper mapper, FraudDetectionService fraudService) {
        this.repository = repository;
        this.mapper = mapper;
        this.fraudService = fraudService;
    }

    public TransactionResponseDTO create(TransactionRequestDTO transactionDTO) {
        TransactionModel transaction = mapper.toModel(transactionDTO);
        transaction.setDate(Instant.now());

        if (fraudService.isSuspect(transaction)) {
            transaction.setSuspect(true);
            // Send async message to SQS
        }

        return mapper.toResponseDTO(repository.save(transaction));
    }

    public List<TransactionResponseDTO> getTransactionsByCpf(String cpf) {
        List<TransactionModel> transactions = repository.findAllByCpf(cpf);

        return transactions.stream().map(mapper::toResponseDTO).toList();
    }

    public TransactionResponseDTO getTransactionById(Long id) throws Exception {
        Optional<TransactionModel> transaction = repository.findById(id);

        if (transaction.isEmpty()) {
            throw new Exception("Transaction with id :: " + id + " not found");
        }

        return mapper.toResponseDTO(transaction.get());
    }

    public List<TransactionResponseDTO> getSuspectTransactions() {
        List<TransactionModel> transactions = repository.findAllBySuspect(true);

        return transactions.stream().map(mapper::toResponseDTO).toList();
    }

    public TransactionsStatisticsDTO getTransactionsStats() {
        List<BigDecimal> values = repository.findAll()
                .stream()
                .map(TransactionModel::getValue)
                .toList();

        return StatisticalUtils.describeValues(values);
    }
}
