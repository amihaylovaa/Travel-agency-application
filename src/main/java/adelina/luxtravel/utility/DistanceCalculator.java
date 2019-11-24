package adelina.luxtravel.utility;

import static java.lang.Math.*;
import static adelina.luxtravel.utility.Constants.EARTH_RADIUS_IN_METERS;

public class DistanceCalculator {

    public static final double findDistance(double longitudeSource, double latitudeSource,
                                            double longitudeDestination, double latitudeDestination) {
        double latitudeDifference = Math.toRadians(abs(latitudeDestination - latitudeSource));
        double longitudeDifference = Math.toRadians(abs(longitudeDestination - longitudeSource));
        double a = sin(latitudeDifference / 2) * sin(latitudeDifference / 2) +
                cos(Math.toRadians(latitudeSource)) * cos(Math.toRadians(latitudeDestination)) *
                        sin(longitudeDifference / 2) * sin(longitudeDifference / 2);
        double c = 2 * atan2(sqrt(a), sqrt(1 - a));
        return EARTH_RADIUS_IN_METERS * c;
    }
}