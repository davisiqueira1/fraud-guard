package com.davisiqueira.fraud_guard.service.fraud;

import com.davisiqueira.fraud_guard.model.TransactionModel;
import com.davisiqueira.fraud_guard.repository.TransactionRepository;
import com.davisiqueira.fraud_guard.util.StatisticalUtils;
import com.davisiqueira.fraud_guard.util.geolocalization.Coordinates;
import com.davisiqueira.fraud_guard.util.geolocalization.GeolocalizationUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FraudDetectionService {
    private final TransactionRepository repository;
    private static final int SAMPLE_SIZE = 10;
    private static final double ALLOWED_RADIUS = 1000;
    private static final int FREQUENCY_TIME_WINDOW_MINUTES = 5;
    private static final int MAX_TRANSACTIONS_ALLOWED_IN_WINDOW = 5;


    public FraudDetectionService(TransactionRepository repository) {
        this.repository = repository;
    }

    public boolean isSuspect(TransactionModel transaction) {
        int score = 0;

        List<TransactionModel> transactions = repository.getRandomSample(SAMPLE_SIZE);

        if (transactions.isEmpty()) {
            return false;
        }

        if (isHighFrequency()) {
            score += 10;
        }

        if (isOutlierValue(transactions, transaction)) {
            score += 50;
        }

        if (isOutOfAllowedRadius(transactions, transaction)) {
            score += 40;
        }

        return score >= 50;
    }

    private boolean isHighFrequency() {
        int countLastFiveMinutes = repository.countTransactionSince(LocalDateTime.now().minusMinutes(FREQUENCY_TIME_WINDOW_MINUTES));

        return countLastFiveMinutes >= MAX_TRANSACTIONS_ALLOWED_IN_WINDOW;
    }

    private boolean isOutlierValue(List<TransactionModel> transactions, TransactionModel transaction) {
        if (transactions.isEmpty()) {
            return false;
        }

        List<BigDecimal> values = transactions
                .stream()
                .map(TransactionModel::getValue)
                .toList();

        BigDecimal average = values
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(values.size()), RoundingMode.HALF_UP);

        BigDecimal standardDeviation = StatisticalUtils.calculateStandardDeviation(values);

        return isSuspectValue(transaction.getValue(), average, standardDeviation);
    }

    private boolean isSuspectValue(BigDecimal value, BigDecimal average, BigDecimal standardDeviation) {
        BigDecimal thresholdMultiplier = BigDecimal.valueOf(3);

        BigDecimal threshold = average.add(standardDeviation.multiply(thresholdMultiplier));

        int comparison = value.compareTo(threshold);

        return comparison >= 0;
    }

    private boolean isOutOfAllowedRadius(List<TransactionModel> transactions, TransactionModel transaction) {
        List<Coordinates<Double>> coordinates = transactions
                .stream()
                .map(t -> new Coordinates<>(t.getLat(), t.getLon()))
                .toList();

        Coordinates<Double> point = new Coordinates<>(transaction.getLat(), transaction.getLon());

        return !GeolocalizationUtils.isPointNearGroup(point, coordinates, ALLOWED_RADIUS);
    }
}
