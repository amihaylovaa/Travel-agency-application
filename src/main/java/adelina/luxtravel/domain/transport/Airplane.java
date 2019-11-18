package adelina.luxtravel.domain.transport;

import adelina.luxtravel.domain.City;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalTime;

import static adelina.luxtravel.utility.Constants.AIRPLANE_MAX_SPEED;

@Entity
@Table(name = "airplane")
@Getter
public class Airplane extends Vehicle {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private long id;

    public Airplane(VehicleClass vehicleClass) {
        super(vehicleClass);
    }

    @Override
    public void calculateDuration(City to) {
        //double duration = to.getDistance() / AIRPLANE_MAX_SPEED;
        ;
        //return parseToLocalTime(duration);
    }
}