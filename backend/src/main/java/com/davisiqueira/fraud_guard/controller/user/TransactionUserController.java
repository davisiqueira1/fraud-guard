package com.davisiqueira.fraud_guard.controller.user;

import com.davisiqueira.fraud_guard.common.response.ApiResponse;
import com.davisiqueira.fraud_guard.dto.transaction.TransactionRequestDTO;
import com.davisiqueira.fraud_guard.dto.transaction.TransactionResponseDTO;
import com.davisiqueira.fraud_guard.dto.transaction.TransactionsStatisticsDTO;
import com.davisiqueira.fraud_guard.security.AuthenticatedUser;
import com.davisiqueira.fraud_guard.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public ResponseEntity<ApiResponse<TransactionResponseDTO>> create(@RequestBody @Valid TransactionRequestDTO transaction) {
        TransactionResponseDTO created = service.create(transaction, authenticatedUser.get().getUser().getId());
        return new ResponseEntity<>(ApiResponse.of(created), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TransactionResponseDTO>>> getTransactionsByUser(@PageableDefault() Pageable page) {
        Page<TransactionResponseDTO> transactions = service.getTransactionsByUserId(authenticatedUser.get().getUser().getId(), page);

        return new ResponseEntity<>(ApiResponse.of(transactions.getContent(), transactions.getPageable()), HttpStatus.OK);
    }

    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<TransactionsStatisticsDTO>> getTransactionsStatsByUser() {
        TransactionsStatisticsDTO stats = service.getTransactionsStats(authenticatedUser.get().getUser().getId());

        return new ResponseEntity<>(ApiResponse.of(stats), HttpStatus.OK);
    }
}
