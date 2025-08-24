package com.davisiqueira.fraud_guard.service;

import com.davisiqueira.fraud_guard.dto.transaction.TransactionRequestDTO;
import com.davisiqueira.fraud_guard.dto.transaction.TransactionResponseDTO;
import com.davisiqueira.fraud_guard.dto.transaction.TransactionsStatisticsDTO;
import com.davisiqueira.fraud_guard.exception.UserNotFoundException;
import com.davisiqueira.fraud_guard.mapper.TransactionMapper;
import com.davisiqueira.fraud_guard.model.TransactionModel;
import com.davisiqueira.fraud_guard.model.UserModel;
import com.davisiqueira.fraud_guard.repository.TransactionRepository;
import com.davisiqueira.fraud_guard.repository.UserRepository;
import com.davisiqueira.fraud_guard.service.fraud.FraudDetectionService;
import com.davisiqueira.fraud_guard.util.StatisticalUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper mapper;
    private final FraudDetectionService fraudService;
    private final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository, TransactionMapper mapper, FraudDetectionService fraudService, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.mapper = mapper;
        this.fraudService = fraudService;
        this.userRepository = userRepository;
    }

    public TransactionResponseDTO create(TransactionRequestDTO transactionDTO, Long userId) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id ::" + userId + "was not found."));

        TransactionModel transaction = mapper.toModel(transactionDTO);
        transaction.setDate(Instant.now());

        if (fraudService.isSuspect(transaction)) {
            transaction.setSuspect(true);
            // Send async message to SQS
        }

        transaction.setUser(user);

        return mapper.toResponseDTO(transactionRepository.save(transaction));
    }

    public List<TransactionResponseDTO> getTransactionsByUserId(Long userId) {
        List<TransactionModel> transactions = transactionRepository.findAllByUserId(userId);

        return transactions.stream().map(mapper::toResponseDTO).toList();
    }

    public TransactionResponseDTO getTransactionById(Long id) throws Exception {
        Optional<TransactionModel> transaction = transactionRepository.findById(id);

        if (transaction.isEmpty()) {
            throw new Exception("Transaction with id :: " + id + " not found");
        }

        return mapper.toResponseDTO(transaction.get());
    }

    public List<TransactionResponseDTO> getSuspectTransactions() {
        List<TransactionModel> transactions = transactionRepository.findAllBySuspect(true);

        return transactions.stream().map(mapper::toResponseDTO).toList();
    }

    public TransactionsStatisticsDTO getTransactionsStats(Long userId) {
        List<BigDecimal> values = transactionRepository.findAllByUserId(userId)
                .stream()
                .map(TransactionModel::getValue)
                .toList();

        return StatisticalUtils.describeValues(values);
    }
}
