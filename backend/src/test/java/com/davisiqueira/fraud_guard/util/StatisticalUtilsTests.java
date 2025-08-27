package com.davisiqueira.fraud_guard.util;

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
}
