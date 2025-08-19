package com.davisiqueira.fraud_guard.util;

import com.davisiqueira.fraud_guard.dto.transaction.TransactionsStatisticsDTO;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

public final class StatisticalUtils {
    private StatisticalUtils() {}

    public static TransactionsStatisticsDTO describeValues(List<BigDecimal> values) {
        final BigDecimal average = calculateAverage(values);
        final BigDecimal variance = calculateVariance(values, average);
        final BigDecimal standardDeviation = calculateStandardDeviation(values);
        final BigDecimal minimum = getMinValue(values);
        final BigDecimal maximum = getMaxValue(values);
        final long count = values.size();

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
        BigDecimal average = calculateAverage(values);
        BigDecimal variance = calculateVariance(values, average);

        return calculateStandardDeviation(variance);

    }

    private static BigDecimal calculateAverage(List<BigDecimal> values) {
        return values
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(values.size()), RoundingMode.HALF_UP);
    }

    private static BigDecimal calculateVariance(List<BigDecimal> values, BigDecimal average) {
        return values
                .stream()
                .map(value -> value.subtract(average).pow(2))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(values.size() - 1), RoundingMode.HALF_UP);
    }

    private static BigDecimal calculateStandardDeviation(BigDecimal variance) {
        return variance.sqrt(new MathContext(10, RoundingMode.HALF_UP));
    }

    private static BigDecimal getMinValue(List<BigDecimal> values) {
        if (values.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return values.stream()
                .filter(Objects::nonNull)
                .reduce(BigDecimal::min)
                .orElse(BigDecimal.ZERO);

    }

    private static BigDecimal getMaxValue(List<BigDecimal> values) {
        if (values.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return values.stream()
                .filter(Objects::nonNull)
                .reduce(BigDecimal::max)
                .orElse(BigDecimal.ZERO);
    }
}
