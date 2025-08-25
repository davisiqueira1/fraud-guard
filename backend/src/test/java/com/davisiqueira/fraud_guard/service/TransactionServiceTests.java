package com.davisiqueira.fraud_guard.service;

import com.davisiqueira.fraud_guard.dto.transaction.TransactionResponseDTO;
import com.davisiqueira.fraud_guard.exception.TransactionNotFoundException;
import com.davisiqueira.fraud_guard.mapper.TransactionMapper;
import com.davisiqueira.fraud_guard.model.TransactionModel;
import com.davisiqueira.fraud_guard.repository.TransactionRepository;
import com.davisiqueira.fraud_guard.repository.UserRepository;
import com.davisiqueira.fraud_guard.service.fraud.FraudDetectionService;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTests {
    @Mock private TransactionRepository transactionRepository;
    @Mock private TransactionMapper mapper = Mappers.getMapper(TransactionMapper.class);
    @Mock private FraudDetectionService fraudService;
    @Mock private UserRepository userRepository;

    @InjectMocks
    private TransactionService service;

    @Nested class FindById {
        @Test
        void shouldReturnTransaction_whenIdExists() {
            final Long id = 123L;

            TransactionModel transaction = new TransactionModel();
            transaction.setId(id);

            TransactionResponseDTO dto = new TransactionResponseDTO(id, null, null, null, null, null);

            when(transactionRepository.findById(id)).thenReturn(Optional.of(transaction));
            when(mapper.toResponseDTO(transaction)).thenReturn(dto);

            TransactionResponseDTO result = service.getTransactionById(id);

            assertEquals(id, result.id());

            verify(transactionRepository).findById(id);
            verify(mapper).toResponseDTO(transaction);
        }

        @Test
        void shouldThrowException_whenIdNotFound() {
            final Long id = -1L;
            when(transactionRepository.findById(id)).thenReturn(Optional.empty());

            assertThrows(TransactionNotFoundException.class, () -> service.getTransactionById(id));
        }
    }

}
