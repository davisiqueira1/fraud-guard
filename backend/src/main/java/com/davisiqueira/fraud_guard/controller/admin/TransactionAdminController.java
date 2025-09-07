package com.davisiqueira.fraud_guard.controller.admin;

import com.davisiqueira.fraud_guard.common.error.ApiErrorResponse;
import com.davisiqueira.fraud_guard.common.response.DefaultApiResponse;
import com.davisiqueira.fraud_guard.common.response.PageInfo;
import com.davisiqueira.fraud_guard.dto.transaction.TransactionResponseDTO;
import com.davisiqueira.fraud_guard.dto.transaction.TransactionsStatisticsDTO;
import com.davisiqueira.fraud_guard.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "Get user transactions",
            description = "Retrieve a paginated list of transactions based on the user with the provided id.",
            tags = {"Administrator"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TransactionResponseDTO.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    @GetMapping
    public ResponseEntity<DefaultApiResponse<List<TransactionResponseDTO>>> getTransactionsByUser(
            @PathVariable Long userId,
            @PageableDefault() Pageable page
    ) {
        Page<TransactionResponseDTO> transactions = service.getTransactionsByUserId(userId, page);

        return new ResponseEntity<>(DefaultApiResponse.of(transactions.getContent(), PageInfo.from(transactions)), HttpStatus.OK);
    }

    @Operation(
            summary = "Get user suspect transactions",
            description = "Retrieve a list of suspect transactions based on the user with the provided id.",
            tags = {"Administrator"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TransactionResponseDTO.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    @GetMapping("/suspect")
    public ResponseEntity<DefaultApiResponse<List<TransactionResponseDTO>>> getSuspectTransactions(@PathVariable Long userId) {
        List<TransactionResponseDTO> suspectTransactions = service.getSuspectTransactions(userId);

        return new ResponseEntity<>(DefaultApiResponse.of(suspectTransactions), HttpStatus.OK);
    }

    @Operation(
            summary = "Get user transactions statistics",
            description = "Calculate user transactions statistics based on all transactions of the user with the provided id.",
            tags = {"Administrator"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TransactionsStatisticsDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    @GetMapping("/statistics")
    public ResponseEntity<DefaultApiResponse<TransactionsStatisticsDTO>> getTransactionsStats(@PathVariable Long userId) {
        TransactionsStatisticsDTO stats = service.getTransactionsStats(userId);

        return new ResponseEntity<>(DefaultApiResponse.of(stats), HttpStatus.OK);
    }
}
