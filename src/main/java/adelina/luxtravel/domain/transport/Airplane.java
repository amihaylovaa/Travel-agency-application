package adelina.luxtravel.domain.transport;

import adelina.luxtravel.domain.TravelingPoint;
import adelina.luxtravel.enumeration.TransportClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalTime;

import static adelina.luxtravel.utility.DistanceCalculator.findDistance;

/**
 * Represents a specific transport - Airplane
 */
@Entity
@Getter
@NoArgsConstructor
@DiscriminatorValue(value = "airplane")
public class Airplane extends Transport {

    @Transient
    private static final double AIRPLANE_AVG_SPEED;

    static {
        AIRPLANE_AVG_SPEED = 850.50;
    }

    public Airplane(TransportClass transportClass) {
        super(transportClass);
    }

    /**
     * {@inheritDoc}
     *
     * @param departurePoint   departure traveling point
     * @param destinationPoint destination traveling point
     * @return
     */
    @Override
    public LocalTime calculateDuration(TravelingPoint departurePoint, TravelingPoint destinationPoint) {
        double departurePointLongitude = departurePoint.getLongitude();
        double departurePointLatitude = departurePoint.getLatitude();
        double destinationPointLongitude = destinationPoint.getLongitude();
        double destinationPointLatitude = destinationPoint.getLatitude();

        double duration = findDistance(departurePointLongitude, departurePointLatitude,
                destinationPointLongitude, destinationPointLatitude) / AIRPLANE_AVG_SPEED;

        return parseToLocalTime(duration);
    }
}