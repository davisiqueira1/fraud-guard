package com.davisiqueira.fraud_guard.controller.admin;

import com.davisiqueira.fraud_guard.common.response.ApiResponse;
import com.davisiqueira.fraud_guard.dto.transaction.TransactionResponseDTO;
import com.davisiqueira.fraud_guard.dto.transaction.TransactionsStatisticsDTO;
import com.davisiqueira.fraud_guard.service.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public ResponseEntity<ApiResponse<Page<TransactionResponseDTO>>> getTransactionsByUser(
            @PathVariable Long userId,
            @PageableDefault() Pageable page
    ) {
        Page<TransactionResponseDTO> transactions = service.getTransactionsByUserId(userId, page);

        return new ResponseEntity<>(ApiResponse.of(transactions), HttpStatus.OK);
    }

    @GetMapping("/suspect")
    public ResponseEntity<ApiResponse<List<TransactionResponseDTO>>> getSuspectTransactions(@PathVariable Long userId) {
        List<TransactionResponseDTO> suspectTransactions = service.getSuspectTransactions(userId);

        return new ResponseEntity<>(ApiResponse.of(suspectTransactions), HttpStatus.OK);
    }

    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<TransactionsStatisticsDTO>> getTransactionsStats(@PathVariable Long userId) {
        TransactionsStatisticsDTO stats = service.getTransactionsStats(userId);

        return new ResponseEntity<>(ApiResponse.of(stats), HttpStatus.OK);
    }
}
