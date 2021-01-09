package adelina.luxtravel.utility;

import static java.lang.Math.*;

/**
 * Utility class containing a method
 * that calculates the distance between two points (traveling points)
 * of a sphere using Haversine formula
 */
public class DistanceCalculator {

    private static final double EARTH_RADIUS_IN_METERS = 6_371_000.00;
    private static final double KM = 1_000;

    public static double findDistance(double longitudeSource, double latitudeSource,
                                      double longitudeDestination, double latitudeDestination) {
        double latitudeDifference = Math.toRadians(abs(latitudeDestination - latitudeSource));
        double longitudeDifference = Math.toRadians(abs(longitudeDestination - longitudeSource));
        double x = sin(latitudeDifference / 2.0) * sin(latitudeDifference / 2.0) +
                cos(Math.toRadians(latitudeSource)) * cos(Math.toRadians(latitudeDestination)) *
                        sin(longitudeDifference / 2.0) * sin(longitudeDifference / 2.0);
        double dist = 2 * atan2(sqrt(x), sqrt(1 - x));

        return (EARTH_RADIUS_IN_METERS * dist) / KM;
    }

    private DistanceCalculator() {
    }
}