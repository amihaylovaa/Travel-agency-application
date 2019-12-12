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
    public LocalTime calculateDuration(TravelingPoint startingPoint, TravelingPoint targetPoint) {
        double startingPointLongitude = startingPoint.getLongitude();
        double startingPointLatitude = startingPoint.getLatitude();
        double targetPointLongitude = targetPoint.getLongitude();
        double targetPointLatitude = targetPoint.getLatitude();

        double duration = findDistance(startingPointLongitude, startingPointLatitude,
                targetPointLongitude, targetPointLatitude) * BUS_AVG_SPEED;

        return parseToLocalTime(duration);
    }
}