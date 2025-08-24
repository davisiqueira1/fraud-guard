package com.davisiqueira.fraud_guard.controller.admin;

import com.davisiqueira.fraud_guard.dto.transaction.TransactionResponseDTO;
import com.davisiqueira.fraud_guard.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/admin/transactions")
public class TransactionAdminGlobalController {
    private final TransactionService service;

    public TransactionAdminGlobalController(TransactionService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getTransactionById(@PathVariable Long id) throws Exception {
        TransactionResponseDTO transaction = service.getTransactionById(id);

        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }
}
