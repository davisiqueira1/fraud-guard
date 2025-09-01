package com.davisiqueira.fraud_guard.service;

import com.davisiqueira.fraud_guard.dto.transaction.TransactionEventDTO;
import com.davisiqueira.fraud_guard.dto.transaction.TransactionRequestDTO;
import com.davisiqueira.fraud_guard.dto.transaction.TransactionResponseDTO;
import com.davisiqueira.fraud_guard.dto.transaction.TransactionsStatisticsDTO;
import com.davisiqueira.fraud_guard.exception.TransactionNotFoundException;
import com.davisiqueira.fraud_guard.exception.UserNotFoundException;
import com.davisiqueira.fraud_guard.mapper.TransactionMapper;
import com.davisiqueira.fraud_guard.model.TransactionModel;
import com.davisiqueira.fraud_guard.model.UserModel;
import com.davisiqueira.fraud_guard.repository.TransactionRepository;
import com.davisiqueira.fraud_guard.repository.UserRepository;
import com.davisiqueira.fraud_guard.service.cloud.SQSService;
import com.davisiqueira.fraud_guard.service.fraud.FraudDetectionService;
import com.davisiqueira.fraud_guard.util.StatisticalUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final FraudDetectionService fraudService;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final SQSService sqsService;

    public TransactionService(TransactionRepository transactionRepository, TransactionMapper transactionMapper, FraudDetectionService fraudService, UserRepository userRepository, ObjectMapper objectMapper, SQSService sqsService) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
        this.fraudService = fraudService;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
        this.sqsService = sqsService;
    }

    public TransactionResponseDTO create(TransactionRequestDTO transactionDTO, Long userId) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id ::" + userId + "was not found."));

        TransactionModel transaction = transactionMapper.toModel(transactionDTO);

        transaction.setDate(Instant.now());
        transaction.setUser(user);
        transaction.setSuspect(fraudService.isSuspect(transaction));

        TransactionModel saved = transactionRepository.save(transaction);

        if (saved.getSuspect() == true) {
            sendMessageAsync(transactionMapper.toEvent(saved));
        }

        return transactionMapper.toResponseDTO(saved);
    }

    public List<TransactionResponseDTO> getTransactionsByUserId(Long userId) {
        List<TransactionModel> transactions = transactionRepository.findAllByUserId(userId);

        return transactions.stream().map(transactionMapper::toResponseDTO).toList();
    }

    public TransactionResponseDTO getTransactionById(Long id) {
        Optional<TransactionModel> transaction = transactionRepository.findById(id);

        if (transaction.isEmpty()) {
            throw new TransactionNotFoundException("Transaction with id :: " + id + " not found");
        }

        return transactionMapper.toResponseDTO(transaction.get());
    }

    public List<TransactionResponseDTO> getSuspectTransactions(Long userId) {
        List<TransactionModel> transactions = transactionRepository.findAllBySuspectAndUserId(true, userId);

        return transactions.stream().map(transactionMapper::toResponseDTO).toList();
    }

    public TransactionsStatisticsDTO getTransactionsStats(Long userId) {
        List<BigDecimal> values = transactionRepository.findAllByUserId(userId)
                .stream()
                .map(TransactionModel::getValue)
                .toList();

        return StatisticalUtils.describeValues(values);
    }

    private void sendMessageAsync(TransactionEventDTO transaction) {
        try {
            String messageBody = objectMapper.writeValueAsString(transaction);
            sqsService.sendMessageAsync(messageBody, null);
        } catch (JsonProcessingException e) {
            System.getLogger("json").log(System.Logger.Level.ERROR, "Error while serializing transaction to JSON. " + e.getMessage());
        }
    }
}
