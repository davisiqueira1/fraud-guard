package com.davisiqueira.fraud_guard.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

public final class StatisticalUtils {
    private StatisticalUtils() {}

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
}
