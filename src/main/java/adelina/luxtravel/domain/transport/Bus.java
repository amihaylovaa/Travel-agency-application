package adelina.luxtravel.domain.transport;

import adelina.luxtravel.domain.TravelingPoint;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalTime;

import static adelina.luxtravel.utility.Constants.BUS_AVG_SPEED;
import static adelina.luxtravel.utility.DistanceCalculator.findDistance;

/**
 * Represents a specific transport - Bus
 */
@Entity
@NoArgsConstructor
@DiscriminatorValue(value = "bus")
public class Bus extends Transport {
    /**
     * Constructor
     *
     * @param transportClass transport's class
     */
    public Bus(TransportClass transportClass) {
        super(transportClass);
    }

    /**
     * Constructor
     *
     * @param id             bus's id
     * @param transportClass transport's class
     */
    public Bus(long id, TransportClass transportClass) {
        super(id, transportClass);
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
                destinationPointLongitude, destinationPointLatitude) / BUS_AVG_SPEED;

        return parseToLocalTime(duration);
    }
}