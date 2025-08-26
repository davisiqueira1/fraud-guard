package com.davisiqueira.fraud_guard.service.fraud;

import com.davisiqueira.fraud_guard.model.TransactionModel;
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
        List<TransactionModel> sample = new ArrayList<>();

        when(repository.getRandomSample(SAMPLE_SIZE)).thenReturn(sample);

        boolean result = service.isSuspect(null);

        verify(repository).getRandomSample(SAMPLE_SIZE);
        assertFalse(result);
    }

    @Test
    void shouldReturnFalse_whenScoreBelowThreshold() {
        List<TransactionModel> sample = generateDefaultSample();

        TransactionModel transaction = generateDefaultTransaction();

        when(repository.getRandomSample(SAMPLE_SIZE)).thenReturn(sample);
        when(repository.countTransactionSince(any(LocalDateTime.class))).thenReturn(0);

        boolean result = service.isSuspect(transaction);

        verify(repository).getRandomSample(SAMPLE_SIZE);
        verify(repository).countTransactionSince(any(LocalDateTime.class));
        assertFalse(result);
    }

    @Test
    void shouldReturnTrue_whenScoreEqualsThreshold() {
        List<TransactionModel> sample = generateDefaultSample();

        TransactionModel transaction = generateDefaultTransaction();
        // Outlier value.
        transaction.setValue(BigDecimal.valueOf(Long.MAX_VALUE));

        when(repository.getRandomSample(SAMPLE_SIZE)).thenReturn(sample);
        when(repository.countTransactionSince(any(LocalDateTime.class))).thenReturn(0);

        boolean result = service.isSuspect(transaction);

        verify(repository).getRandomSample(SAMPLE_SIZE);
        verify(repository).countTransactionSince(any(LocalDateTime.class));
        assertTrue(result);
    }

    @Test
    void shouldReturnTrue_whenScoreAboveThreshold() {
        List<TransactionModel> sample = generateDefaultSample();

        TransactionModel transaction = generateDefaultTransaction();
        // Outlier value.
        transaction.setValue(BigDecimal.valueOf(Long.MAX_VALUE));

        when(repository.getRandomSample(SAMPLE_SIZE)).thenReturn(sample);
        // High frequency.
        when(repository.countTransactionSince(any(LocalDateTime.class))).thenReturn(Integer.MAX_VALUE);

        boolean result = service.isSuspect(transaction);

        verify(repository).getRandomSample(SAMPLE_SIZE);
        verify(repository).countTransactionSince(any(LocalDateTime.class));
        assertTrue(result);
    }

    private TransactionModel generateDefaultTransaction() {
        return new TransactionModel(
                null,
                null,
                null,
                BigDecimal.valueOf(10),
                0.0,
                0.0,
                false,
                null
        );
    }

    private List<TransactionModel> generateDefaultSample() {
        List<TransactionModel> sample = new ArrayList<>();
        for (int i = 0; i < SAMPLE_SIZE; i++) {
            sample.add(generateDefaultTransaction());
        }

        return sample;
    }

}