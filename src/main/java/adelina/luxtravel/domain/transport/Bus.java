package adelina.luxtravel.domain.transport;

import adelina.luxtravel.domain.City;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalTime;

import static adelina.luxtravel.utility.Constants.BUS_MAX_SPEED;

@Entity
@Table(name = "bus")
public class Bus extends Vehicle {
    public Bus(String brandName) {
        super(brandName);
    }

    @Override
    public void calculateDuration(City to) {
      //  double duration = to.getDistance() / BUS_MAX_SPEED;
;
     //   return parseToLocalTime(duration);
    }
}