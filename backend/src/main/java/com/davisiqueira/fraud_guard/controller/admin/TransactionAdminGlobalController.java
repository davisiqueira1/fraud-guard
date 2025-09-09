package com.davisiqueira.fraud_guard.controller.admin;

import com.davisiqueira.fraud_guard.common.error.ApiErrorResponse;
import com.davisiqueira.fraud_guard.common.response.DefaultApiResponse;
import com.davisiqueira.fraud_guard.dto.transaction.TransactionResponseDTO;
import com.davisiqueira.fraud_guard.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Administrator", description = "Administrator operations (requires ROLE_ADMINISTRATOR)")
@Controller
@RequestMapping("/api/admin/transactions")
public class TransactionAdminGlobalController {
    private final TransactionService service;

    public TransactionAdminGlobalController(TransactionService service) {
        this.service = service;
    }

    @Operation(
            summary = "Get transaction by id",
            description = "Get transaction based on the provided id.",
            tags = {"Administrator"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TransactionResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<DefaultApiResponse<TransactionResponseDTO>> getTransactionById(@PathVariable Long id) {
        TransactionResponseDTO transaction = service.getTransactionById(id);

        return new ResponseEntity<>(DefaultApiResponse.of(transaction), HttpStatus.OK);
    }
}
