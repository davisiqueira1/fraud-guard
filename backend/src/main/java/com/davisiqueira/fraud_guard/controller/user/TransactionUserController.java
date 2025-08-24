package com.davisiqueira.fraud_guard.controller.user;

import com.davisiqueira.fraud_guard.dto.transaction.TransactionRequestDTO;
import com.davisiqueira.fraud_guard.dto.transaction.TransactionResponseDTO;
import com.davisiqueira.fraud_guard.dto.transaction.TransactionsStatisticsDTO;
import com.davisiqueira.fraud_guard.security.AuthenticatedUser;
import com.davisiqueira.fraud_guard.service.TransactionService;
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
public class TransactionUserController {
    private final AuthenticatedUser authenticatedUser;
    private final TransactionService service;

    public TransactionUserController(TransactionService service, AuthenticatedUser authenticatedUser) {
        this.service = service;
        this.authenticatedUser = authenticatedUser;
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> create(@RequestBody @Valid TransactionRequestDTO transaction) {
        TransactionResponseDTO created = service.create(transaction, authenticatedUser.get().getUser().getId());
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionsByUser() {
        List<TransactionResponseDTO> transactions = service.getTransactionsByUserId(authenticatedUser.get().getUser().getId());

        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/statistics")
    public ResponseEntity<TransactionsStatisticsDTO> getTransactionsStatsByUser() {
        TransactionsStatisticsDTO stats = service.getTransactionsStats(authenticatedUser.get().getUser().getId());

        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
}
