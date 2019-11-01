package adelina.luxtravel.domain.transport;

import adelina.luxtravel.domain.City;

import java.time.LocalDate;
import java.time.LocalTime;
import static adelina.luxtravel.utility.Constants.BUS_MAX_SPEED;

public class Bus extends Vehicle {
    public Bus(String brandName) {
        super(brandName);
    }

    @Override
    public LocalTime calculateDuration(City to) {
        double duration = to.getDistance() / BUS_MAX_SPEED;

        return parseToLocalTime(duration);
    }
}
