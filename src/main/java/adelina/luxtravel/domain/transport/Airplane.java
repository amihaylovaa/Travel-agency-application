package adelina.luxtravel.domain.transport;


import adelina.luxtravel.domain.City;
import adelina.luxtravel.exception.FailedInitializationException;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

import static adelina.luxtravel.utility.Constants.AIRPLANE_MAX_SPEED;

@Getter
public class Airplane extends Vehicle {
    private AirplaneClass airplaneClass;

    public Airplane(LocalDate releaseDate, java.lang.String brandName, AirplaneClass airplaneClass) {
        super(releaseDate, brandName);
        initializeAirplaneClass(airplaneClass);
    }

    private void initializeAirplaneClass(AirplaneClass airplaneClass) {
        if (airplaneClass == null) {
            throw new FailedInitializationException("Airplane class is not set");
        }
        this.airplaneClass = airplaneClass;
    }

    @Override
    public LocalTime calculateDuration(City to) {
        double duration = to.getDistance() / AIRPLANE_MAX_SPEED;

        return parseToLocalTime(duration);
    }
}