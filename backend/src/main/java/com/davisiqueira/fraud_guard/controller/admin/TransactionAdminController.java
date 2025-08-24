package com.davisiqueira.fraud_guard.controller.admin;

import com.davisiqueira.fraud_guard.dto.transaction.TransactionResponseDTO;
import com.davisiqueira.fraud_guard.dto.transaction.TransactionsStatisticsDTO;
import com.davisiqueira.fraud_guard.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Validated
@RequestMapping("/api/users/{userId}/transactions")
public class TransactionAdminController {
    private final TransactionService service;

    public TransactionAdminController(TransactionService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionsByUser(@PathVariable Long userId) {
        List<TransactionResponseDTO> transactions = service.getTransactionsByUserId(userId);

        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getTransactionByID(@PathVariable Long userId, @PathVariable Long id) throws Exception {
        TransactionResponseDTO transaction = service.getTransactionById(id);

        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @GetMapping("/suspect")
    public ResponseEntity<List<TransactionResponseDTO>> getSuspectTransactions(@PathVariable Long userId) {
        List<TransactionResponseDTO> suspectTransactions = service.getSuspectTransactions(userId);

        return new ResponseEntity<>(suspectTransactions, HttpStatus.OK);
    }

    @GetMapping("/statistics")
    public ResponseEntity<TransactionsStatisticsDTO> getTransactionsStats(@PathVariable Long userId) {
        TransactionsStatisticsDTO stats = service.getTransactionsStats(userId);

        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
}
