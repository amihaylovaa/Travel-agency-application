package adelina.luxtravel.utility;

import static adelina.luxtravel.utility.Constants.KM;
import static java.lang.Math.*;
import static adelina.luxtravel.utility.Constants.EARTH_RADIUS_IN_METERS;

/**
 * Utility class contains a single method
 * that calculates the distance between two points(traveling points)
 * of sphere by Haversine formula
 */
public class DistanceCalculator {

    public static final double findDistance(double longitudeSource, double latitudeSource,
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