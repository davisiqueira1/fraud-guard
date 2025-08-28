package com.davisiqueira.fraud_guard.util.geolocalization;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GeolocalizationUtilsTests {
    final double RADIUS = 1000.0;

    @Nested
    class PointNearGroup {
        @Nested
        class Boundary {
            @Test
            void shouldReturnFalse_whenCoordinatesIsEmpty() {
                List<Coordinates<Double>> coordinates = new ArrayList<>();
                Coordinates<Double> point = new Coordinates<>(0.0, 0.0);

                boolean result = GeolocalizationUtils.isPointNearGroup(point, coordinates, RADIUS);

                assertFalse(result);
            }

            @Test
            void shouldThrowNullPointerException_whenCoordinatesIsNull() {
                Coordinates<Double> point = new Coordinates<>(0.0, 0.0);

                assertThrows(NullPointerException.class, () -> GeolocalizationUtils.isPointNearGroup(point, null, RADIUS));
            }

            @Test
            void shouldThrowNullPointerException_whenPointIsNull() {
                List<Coordinates<Double>> coordinates = new ArrayList<>();

                assertThrows(NullPointerException.class, () -> GeolocalizationUtils.isPointNearGroup(null, coordinates, RADIUS));
            }

            @Test
            void shouldIgnoreNullValuesInList() {
                List<Coordinates<Double>> coordinates = new ArrayList<>() {{
                    add(null);
                    add(null);
                    add(null);
                }};

                Coordinates<Double> point = new Coordinates<>(0.0, 0.0);

                boolean result = GeolocalizationUtils.isPointNearGroup(point, coordinates, RADIUS);

                assertFalse(result);
            }
        }
    }
}
