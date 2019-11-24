package adelina.luxtravel.domain.transport;

import adelina.luxtravel.domain.TravelingPoint;
import lombok.Getter;

import javax.persistence.*;

import java.time.LocalTime;

import static adelina.luxtravel.utility.Constants.AIRPLANE_AVG_SPEED;
import static adelina.luxtravel.utility.DistanceCalculator.findDistance;

@Entity
@Table(name = "airplane")
@Getter
public class Airplane extends Transport {
    @OneToOne
    @JoinColumn(name = "id")
    private long id;

    public Airplane(TransportClass vehicleClass) {
        super(vehicleClass);
    }

    @Override
    public LocalTime calculateDuration(TravelingPoint source, TravelingPoint destination) {
        double sourceLongitude = source.getLongitude();
        double sourceLatitude = source.getLatitude();
        double destinationLongitude = destination.getLongitude();
        double destinationLatitude = destination.getLatitude();

        double duration = findDistance(sourceLongitude, sourceLatitude,
                destinationLongitude, destinationLatitude) * AIRPLANE_AVG_SPEED;

        return parseToLocalTime(duration);
    }
}