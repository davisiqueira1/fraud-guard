package com.davisiqueira.fraud_guard.controller;

import com.davisiqueira.fraud_guard.dto.transaction.TransactionRequestDTO;
import com.davisiqueira.fraud_guard.dto.transaction.TransactionResponseDTO;
import com.davisiqueira.fraud_guard.dto.transaction.TransactionsStatisticsDTO;
import com.davisiqueira.fraud_guard.security.AuthenticatedUser;
import com.davisiqueira.fraud_guard.service.TransactionService;
import com.davisiqueira.fraud_guard.validation.validator.cpf.ValidCpf;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Validated
@RequestMapping("/api/transactions")
public class TransactionController {
    private final AuthenticatedUser authenticatedUser;
    private final TransactionService service;

    public TransactionController(TransactionService service, AuthenticatedUser authenticatedUser) {
        this.service = service;
        this.authenticatedUser = authenticatedUser;
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> create(@RequestBody @Valid TransactionRequestDTO transaction) {
        TransactionResponseDTO created = service.create(transaction, authenticatedUser.get().getUser().getId());
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionsByCpf(@RequestParam("cpf") @ValidCpf String cpf) {
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
