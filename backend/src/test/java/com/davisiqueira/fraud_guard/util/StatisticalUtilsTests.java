package com.davisiqueira.fraud_guard.util;

import com.davisiqueira.fraud_guard.dto.transaction.TransactionsStatisticsDTO;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatisticalUtilsTests {
    @Nested
    class StandardDeviation {
        @Nested
        class Sanity {
            @Test
            void shouldReturnOne_whenUsingBasicExample() {
                List<BigDecimal> values = new ArrayList<>() {{
                    add(BigDecimal.ONE);
                    add(BigDecimal.valueOf(2));
                    add(BigDecimal.valueOf(3));
                }};

                BigDecimal result = StatisticalUtils.calculateStandardDeviation(values);

                assertEquals(BigDecimal.ONE, result);
            }

            @Test
            void shouldReturnZero_whenNonDispersedData() {
                List<BigDecimal> values = new ArrayList<>() {{
                    add(BigDecimal.ONE);
                    add(BigDecimal.ONE);
                    add(BigDecimal.ONE);
                }};

                BigDecimal result = StatisticalUtils.calculateStandardDeviation(values);

                assertEquals(BigDecimal.ZERO, result);
            }

            @Test
            void shouldReturnZero_whenValuesSizeIsLessThan2() {
                List<BigDecimal> values = new ArrayList<>() {{
                    add(BigDecimal.ONE);
                }};

                BigDecimal result = StatisticalUtils.calculateStandardDeviation(values);

                assertEquals(BigDecimal.ZERO, result);
            }
        }

        @Nested
        class Boundary {
            @Test
            void shouldReturnZero_whenValuesIsEmpty() {
                List<BigDecimal> values = new ArrayList<>();

                BigDecimal result = StatisticalUtils.calculateStandardDeviation(values);

                assertEquals(BigDecimal.ZERO, result);
            }

            @Test
            void shouldReturnZero_whenValuesIsNull() {
                BigDecimal result = StatisticalUtils.calculateStandardDeviation(null);

                assertEquals(BigDecimal.ZERO, result);
            }

            @Test
            void shouldIgnoreNullValuesInList() {
                List<BigDecimal> values = new ArrayList<>() {{
                    add(null);
                    add(null);
                    add(null);
                }};

                BigDecimal result = StatisticalUtils.calculateStandardDeviation(values);

                assertEquals(BigDecimal.ZERO, result);
            }

            @Test
            void shouldReturnZero_whenNonNullValuesSizeIsLessThan2() {
                List<BigDecimal> values = new ArrayList<>() {{
                    add(BigDecimal.ZERO);
                    add(null);
                }};

                BigDecimal result = StatisticalUtils.calculateStandardDeviation(values);

                assertEquals(BigDecimal.ZERO, result);
            }
        }

    }

    @Nested
    class DescribeValues {
        @Nested
        class Boundary {
            @Test
            void shouldReturnAllZero_whenValuesIsEmpty() {
                List<BigDecimal> values = new ArrayList<>();

                TransactionsStatisticsDTO result = StatisticalUtils.describeValues(values);

                assertEquals(BigDecimal.ZERO, result.average());
                assertEquals(BigDecimal.ZERO, result.variance());
                assertEquals(BigDecimal.ZERO, result.standardDeviation());
                assertEquals(BigDecimal.ZERO, result.minimum());
                assertEquals(BigDecimal.ZERO, result.maximum());
                assertEquals(0, result.count());
            }

            @Test
            void shouldReturnAllZero_whenValuesIsNull() {
                TransactionsStatisticsDTO result = StatisticalUtils.describeValues(null);

                assertEquals(BigDecimal.ZERO, result.average());
                assertEquals(BigDecimal.ZERO, result.variance());
                assertEquals(BigDecimal.ZERO, result.standardDeviation());
                assertEquals(BigDecimal.ZERO, result.minimum());
                assertEquals(BigDecimal.ZERO, result.maximum());
                assertEquals(0, result.count());
            }
        }
    }
}
