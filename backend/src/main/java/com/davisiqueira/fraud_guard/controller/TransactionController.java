package com.davisiqueira.fraud_guard.controller;

import com.davisiqueira.fraud_guard.dto.transaction.TransactionRequestDTO;
import com.davisiqueira.fraud_guard.dto.transaction.TransactionResponseDTO;
import com.davisiqueira.fraud_guard.dto.transaction.TransactionsStatisticsDTO;
import com.davisiqueira.fraud_guard.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> create(@RequestBody TransactionRequestDTO transaction) {
        TransactionResponseDTO created = service.create(transaction);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionsByCpf(@RequestParam("cpf") String cpf) {
        List<TransactionResponseDTO> transactions = service.getTransactionsByCpf(cpf);

        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getTransactionByID(@PathVariable Long id) throws Exception {
        TransactionResponseDTO transaction = service.getTransactionById(id);

        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @GetMapping("/suspect")
    public ResponseEntity<List<TransactionResponseDTO>> getSuspectTransactions() {
        List<TransactionResponseDTO> suspectTransactions = service.getSuspectTransactions();

        return new ResponseEntity<>(suspectTransactions, HttpStatus.OK);
    }

    @GetMapping("/statistics")
    public ResponseEntity<TransactionsStatisticsDTO> getTransactionsStats() {
        TransactionsStatisticsDTO stats = service.getTransactionsStats();

        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
}
