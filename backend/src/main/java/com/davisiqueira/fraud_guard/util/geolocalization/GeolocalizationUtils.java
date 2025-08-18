package com.davisiqueira.fraud_guard.util.geolocalization;

import java.util.List;

public final class GeolocalizationUtils {
    private static final double EARTH_RADIUS_IN_KM = 6371008.8;

    public static boolean isPointNearGroup(Coordinates<Double> point, List<Coordinates<Double>> coordinates, double radius) {
        Coordinates<Double> center = calculateCenterPoint(coordinates);
        return isPointOnRadius(point, center, radius);
    }

    private static Coordinates<Double> calculateCenterPoint(List<Coordinates<Double>> coordinates) {
        double x = 0;
        double y = 0;
        double z = 0;

        for (Coordinates<Double> point : coordinates) {
            double latRad = Math.toRadians(point.lat());
            double lonRad = Math.toRadians(point.lon());

            double _x = Math.cos(latRad) * Math.cos(lonRad);
            double _y = Math.cos(latRad) * Math.sin(lonRad);
            double _z = Math.sin(latRad);

            x += _x;
            y += _y;
            z += _z;
        }

        int total = coordinates.size();
        x /= total;
        y /= total;
        z /= total;

        double lon = Math.atan2(y, x);
        double hyp = Math.sqrt(x * x + y * y);
        double lat = Math.atan2(z, hyp);

        return new Coordinates<>(Math.toDegrees(lat), Math.toDegrees(lon));
    }

    private static boolean isPointOnRadius(Coordinates<Double> point, Coordinates<Double> center, double radiusMeters) {
        double d = distanceMeters(center, point);
        return d <= radiusMeters;
    }

    private static double distanceMeters(Coordinates<Double> point1, Coordinates<Double> point2) {
        double phi1 = Math.toRadians(point1.lat());
        double phi2 = Math.toRadians(point2.lat());

        double deltaPhi = phi2 - phi1;
        double deltaLambda = Math.toRadians(point2.lon() - point1.lon());

        double sinDeltaPhi = Math.sin(deltaPhi / 2);
        double sinDeltaLambda = Math.sin(deltaLambda / 2);

        double a = sinDeltaPhi * sinDeltaPhi + Math.cos(phi1) * Math.cos(phi2) * sinDeltaLambda * sinDeltaLambda;

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_IN_KM * c;
    }
}
