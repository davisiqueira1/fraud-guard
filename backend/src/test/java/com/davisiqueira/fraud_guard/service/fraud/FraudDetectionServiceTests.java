package com.davisiqueira.fraud_guard.service.fraud;

import com.davisiqueira.fraud_guard.model.TransactionModel;
import com.davisiqueira.fraud_guard.model.UserModel;
import com.davisiqueira.fraud_guard.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FraudDetectionServiceTests {
    private static final int SAMPLE_SIZE = 10;

    @Mock
    private TransactionRepository repository;

    @InjectMocks
    private FraudDetectionService service;

    @Test
    void shouldReturnFalse_whenSampleIsEmpty() {
        final Long userId = 1L;
        List<TransactionModel> sample = new ArrayList<>();

        TransactionModel transaction = generateDefaultTransaction(userId);

        when(repository.getRandomSampleFromUser(SAMPLE_SIZE, userId)).thenReturn(sample);

        boolean result = service.isSuspect(transaction);

        verify(repository).getRandomSampleFromUser(SAMPLE_SIZE, userId);
        assertFalse(result);
    }

    @Test
    void shouldReturnFalse_whenScoreBelowThreshold() {
        final Long userId = 1L;
        List<TransactionModel> sample = generateDefaultSample(userId);

        TransactionModel transaction = generateDefaultTransaction(userId);

        when(repository.getRandomSampleFromUser(SAMPLE_SIZE, userId)).thenReturn(sample);
        when(repository.countUserTransactionsSince(any(LocalDateTime.class), eq(userId))).thenReturn(0);

        boolean result = service.isSuspect(transaction);

        verify(repository).getRandomSampleFromUser(SAMPLE_SIZE, userId);
        verify(repository).countUserTransactionsSince(any(LocalDateTime.class), eq(userId));
        assertFalse(result);
    }

    @Test
    void shouldReturnTrue_whenScoreEqualsThreshold() {
        final Long userId = 1L;
        List<TransactionModel> sample = generateDefaultSample(userId);

        TransactionModel transaction = generateDefaultTransaction(userId);
        // Outlier value.
        transaction.setValue(BigDecimal.valueOf(Long.MAX_VALUE));

        when(repository.getRandomSampleFromUser(SAMPLE_SIZE, userId)).thenReturn(sample);
        when(repository.countUserTransactionsSince(any(LocalDateTime.class), eq(userId))).thenReturn(0);

        boolean result = service.isSuspect(transaction);

        verify(repository).getRandomSampleFromUser(SAMPLE_SIZE, userId);
        verify(repository).countUserTransactionsSince(any(LocalDateTime.class), eq(userId));
        assertTrue(result);
    }

    @Test
    void shouldReturnTrue_whenScoreAboveThreshold() {
        final Long userId = 1L;
        List<TransactionModel> sample = generateDefaultSample(userId);

        TransactionModel transaction = generateDefaultTransaction(userId);
        // Outlier value.
        transaction.setValue(BigDecimal.valueOf(Long.MAX_VALUE));

        when(repository.getRandomSampleFromUser(SAMPLE_SIZE, userId)).thenReturn(sample);
        // High frequency.
        when(repository.countUserTransactionsSince(any(LocalDateTime.class), eq(userId))).thenReturn(Integer.MAX_VALUE);

        boolean result = service.isSuspect(transaction);

        verify(repository).getRandomSampleFromUser(SAMPLE_SIZE, userId);
        verify(repository).countUserTransactionsSince(any(LocalDateTime.class), eq(userId));
        assertTrue(result);
    }

    private TransactionModel generateDefaultTransaction(Long userId) {
        return new TransactionModel(
                null,
                null,
                null,
                BigDecimal.valueOf(10),
                0.0,
                0.0,
                false,
                UserModel.builder().id(userId).build()
        );
    }

    private List<TransactionModel> generateDefaultSample(Long userId) {
        List<TransactionModel> sample = new ArrayList<>();
        for (int i = 0; i < SAMPLE_SIZE; i++) {
            sample.add(generateDefaultTransaction(userId));
        }

        return sample;
    }

}