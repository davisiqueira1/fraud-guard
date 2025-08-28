package com.davisiqueira.fraud_guard.util;

import com.davisiqueira.fraud_guard.dto.transaction.TransactionsStatisticsDTO;
import lombok.NonNull;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

public final class StatisticalUtils {
    private StatisticalUtils() {
    }

    public static TransactionsStatisticsDTO describeValues(@NonNull List<BigDecimal> values) {
        final List<BigDecimal> filteredValues = values.stream().filter(Objects::nonNull).toList();

        final BigDecimal average = calculateAverage(filteredValues);
        final BigDecimal variance = calculateVariance(filteredValues, average);
        final BigDecimal standardDeviation = calculateStandardDeviation(variance);
        final BigDecimal minimum = getMinValue(filteredValues);
        final BigDecimal maximum = getMaxValue(filteredValues);
        final long count = filteredValues.size();

        return new TransactionsStatisticsDTO(
                average,
                variance,
                standardDeviation,
                minimum,
                maximum,
                count
        );
    }

    public static BigDecimal calculateStandardDeviation(List<BigDecimal> values) {
        if (values == null || values.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal average = calculateAverage(values);
        BigDecimal variance = calculateVariance(values, average);

        return calculateStandardDeviation(variance);

    }

    private static BigDecimal calculateAverage(List<BigDecimal> values) {
        if (values == null || values.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return values
                .stream()
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(values.size()), RoundingMode.HALF_UP);
    }

    private static BigDecimal calculateVariance(List<BigDecimal> values, BigDecimal average) {
        if (values == null || values.size() < 2) {
            return BigDecimal.ZERO;
        }

        return values
                .stream()
                .filter(Objects::nonNull)
                .map(value -> value.subtract(average).pow(2))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(values.size() - 1), RoundingMode.HALF_UP);
    }

    private static BigDecimal calculateStandardDeviation(BigDecimal variance) {
        return variance.sqrt(new MathContext(10, RoundingMode.HALF_UP));
    }

    private static BigDecimal getMinValue(List<BigDecimal> values) {
        if (values == null || values.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return values.stream()
                .filter(Objects::nonNull)
                .reduce(BigDecimal::min)
                .orElse(BigDecimal.ZERO);

    }

    private static BigDecimal getMaxValue(List<BigDecimal> values) {
        if (values == null || values.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return values.stream()
                .filter(Objects::nonNull)
                .reduce(BigDecimal::max)
                .orElse(BigDecimal.ZERO);
    }
}
