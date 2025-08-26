package com.davisiqueira.fraud_guard.service;

import com.davisiqueira.fraud_guard.dto.transaction.TransactionRequestDTO;
import com.davisiqueira.fraud_guard.dto.transaction.TransactionResponseDTO;
import com.davisiqueira.fraud_guard.exception.TransactionNotFoundException;
import com.davisiqueira.fraud_guard.exception.UserNotFoundException;
import com.davisiqueira.fraud_guard.mapper.TransactionMapper;
import com.davisiqueira.fraud_guard.model.TransactionModel;
import com.davisiqueira.fraud_guard.model.UserModel;
import com.davisiqueira.fraud_guard.repository.TransactionRepository;
import com.davisiqueira.fraud_guard.repository.UserRepository;
import com.davisiqueira.fraud_guard.service.fraud.FraudDetectionService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTests {
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private TransactionMapper mapper = Mappers.getMapper(TransactionMapper.class);
    @Mock
    private FraudDetectionService fraudService;
    @Mock
    private UserRepository userRepository;

    @Captor
    ArgumentCaptor<TransactionModel> savedCaptor;
    @Captor
    ArgumentCaptor<TransactionModel> returnCaptor;

    @InjectMocks
    private TransactionService service;

    @Nested
    class Create {
        @Test
        void shouldThrowException_whenUserIdNotFound() {
            final Long id = -1L;

            when(userRepository.findById(id)).thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class, () -> service.create(null, id));
            verify(transactionRepository, never()).save(any());
        }

        @Test
        void shouldReturnSuspiciousTransaction_whenSuspicious() {
            final Long userId = 1L;
            final Long transactionId = 2L;

            TransactionModel transaction = new TransactionModel();
            UserModel user = new UserModel();

            TransactionRequestDTO dto = new TransactionRequestDTO(null, null, null, null);

            TransactionModel saved = new TransactionModel();
            saved.setId(transactionId);

            TransactionResponseDTO output = new TransactionResponseDTO(transactionId, null, null, null, null, null);

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(mapper.toModel(dto)).thenReturn(transaction);
            when(fraudService.isSuspect(transaction)).thenReturn(true);
            when(transactionRepository.save(any(TransactionModel.class))).thenReturn(saved);
            when(mapper.toResponseDTO(saved)).thenReturn(output);

            TransactionResponseDTO result = service.create(dto, userId);

            verify(userRepository).findById(userId);
            verify(mapper).toModel(dto);
            verify(fraudService).isSuspect(transaction);

            verify(transactionRepository).save(savedCaptor.capture());
            TransactionModel captured = savedCaptor.getValue();
            assertTrue(captured.getSuspect());
            assertSame(user, captured.getUser());
            assertNotNull(captured.getDate());

            verify(mapper).toResponseDTO(saved);
            assertEquals(transactionId, result.id());
        }

        @Test
        void shouldSaveNonSuspect_whenFraudIsFalse() {
            final Long userId = 1L;
            final Long transactionId = 2L;

            TransactionRequestDTO requestDto = new TransactionRequestDTO(null, null, null, null);
            UserModel user = new UserModel(userId, null, null, null, null);
            TransactionModel mapped = new TransactionModel();
            TransactionModel saved = new TransactionModel();
            saved.setId(2L);
            saved.setUser(user);
            TransactionResponseDTO output = new TransactionResponseDTO(transactionId, null, null, null, null, null);

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(mapper.toModel(requestDto)).thenReturn(mapped);
            when(fraudService.isSuspect(mapped)).thenReturn(false);
            when(transactionRepository.save(any(TransactionModel.class))).thenReturn(saved);
            when(mapper.toResponseDTO(saved)).thenReturn(output);

            TransactionResponseDTO response = service.create(requestDto, userId);

            verify(userRepository).findById(userId);
            verify(mapper).toModel(requestDto);
            verify(fraudService).isSuspect(mapped);

            verify(transactionRepository).save(savedCaptor.capture());
            TransactionModel capturedOnSave = savedCaptor.getValue();
            assertNotEquals(Boolean.TRUE, capturedOnSave.getSuspect());
            assertNotNull(capturedOnSave.getDate());
            assertSame(user, capturedOnSave.getUser());

            verify(mapper).toResponseDTO(returnCaptor.capture());
            TransactionModel captureOnReturn = returnCaptor.getValue();
            assertEquals(transactionId, captureOnReturn.getId());

            assertEquals(transactionId, response.id());
        }
    }

    @Nested
    class FindById {
        @Test
        void shouldReturnTransaction_whenIdExists() {
            final Long id = 123L;

            TransactionModel transaction = new TransactionModel();
            transaction.setId(id);

            TransactionResponseDTO dto = new TransactionResponseDTO(id, null, null, null, null, null);

            when(transactionRepository.findById(id)).thenReturn(Optional.of(transaction));
            when(mapper.toResponseDTO(any(TransactionModel.class))).thenReturn(dto);

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
