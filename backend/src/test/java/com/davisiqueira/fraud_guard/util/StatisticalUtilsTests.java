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
    }
}
