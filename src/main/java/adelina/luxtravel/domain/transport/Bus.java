package adelina.luxtravel.domain.transport;

import adelina.luxtravel.domain.TravelingPoint;
import adelina.luxtravel.utility.DistanceCalculator;

import javax.persistence.*;

import java.time.LocalTime;

import static adelina.luxtravel.utility.Constants.BUS_AVG_SPEED;
import static adelina.luxtravel.utility.DistanceCalculator.findDistance;

@Entity
@Table(name = "bus")
public class Bus extends Transport {
    @OneToOne
    @JoinColumn(name = "id")
    private long id;

    public Bus(TransportClass vehicleClass) {
        super(vehicleClass);
    }

    @Override
    public LocalTime calculateDuration(TravelingPoint departurePoint, TravelingPoint destinationPoint) {
        double departurePointLongitude = departurePoint.getLongitude();
        double departurePointLatitude = departurePoint.getLatitude();
        double destinationPointLongitude = destinationPoint.getLongitude();
        double destinationPointLatitude = destinationPoint.getLatitude();

        double duration = findDistance(departurePointLongitude, departurePointLatitude,
                destinationPointLongitude, destinationPointLatitude) * BUS_AVG_SPEED;

        return parseToLocalTime(duration);
    }
}