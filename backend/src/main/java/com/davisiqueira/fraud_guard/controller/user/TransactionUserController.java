package com.davisiqueira.fraud_guard.controller.user;

import com.davisiqueira.fraud_guard.common.error.ApiErrorResponse;
import com.davisiqueira.fraud_guard.common.response.DefaultApiResponse;
import com.davisiqueira.fraud_guard.common.response.PageInfo;
import com.davisiqueira.fraud_guard.dto.transaction.TransactionRequestDTO;
import com.davisiqueira.fraud_guard.dto.transaction.TransactionResponseDTO;
import com.davisiqueira.fraud_guard.dto.transaction.TransactionsStatisticsDTO;
import com.davisiqueira.fraud_guard.security.AuthenticatedUser;
import com.davisiqueira.fraud_guard.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    @Operation(
            summary = "Creates a transaction",
            description = "Creates a new transaction for the authenticated user based on the provided latitude, longitude and value.",
            tags = {"Transaction"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TransactionResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)
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
    })
    @PostMapping
    public ResponseEntity<DefaultApiResponse<TransactionResponseDTO>> create(@RequestBody @Valid TransactionRequestDTO transaction) {
        TransactionResponseDTO created = service.create(transaction, authenticatedUser.get().getUser().getId());
        return new ResponseEntity<>(DefaultApiResponse.of(created), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get user transactions",
            description = "Retrieve a paginated list of transactions for the authenticated user.",
            tags = {"Transaction"}
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
            ),
    })
    @GetMapping
    public ResponseEntity<DefaultApiResponse<List<TransactionResponseDTO>>> getTransactionsByUser(@PageableDefault() Pageable page) {
        Page<TransactionResponseDTO> transactions = service.getTransactionsByUserId(authenticatedUser.get().getUser().getId(), page);

        return new ResponseEntity<>(DefaultApiResponse.of(transactions.getContent(), PageInfo.from(transactions)), HttpStatus.OK);
    }

    @Operation(
            summary = "Get user transactions statistics",
            description = "Calculate authenticated user's transactions statistics based on all transactions.",
            tags = {"Transaction"}
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
            ),
    })
    @GetMapping("/statistics")
    public ResponseEntity<DefaultApiResponse<TransactionsStatisticsDTO>> getTransactionsStatsByUser() {
        TransactionsStatisticsDTO stats = service.getTransactionsStats(authenticatedUser.get().getUser().getId());

        return new ResponseEntity<>(DefaultApiResponse.of(stats), HttpStatus.OK);
    }
}
