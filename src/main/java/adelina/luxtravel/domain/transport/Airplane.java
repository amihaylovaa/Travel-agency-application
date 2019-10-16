package adelina.luxtravel.domain.transport;

import adelina.luxtravel.exception.FailedInitializationException;

import java.time.LocalDate;

public class Airplane extends Vehicle {
    private AirplaneClass airplaneClass;

    public Airplane(LocalDate releaseDate, String brandName, AirplaneClass airplaneClass) {
        super(releaseDate, brandName);
        initializeAirplaneClass(airplaneClass);
    }

    private void initializeAirplaneClass(AirplaneClass airplaneClass) {
        if (airplaneClass == null) {
            throw new FailedInitializationException("Airplane class is not set");
        }
        this.airplaneClass = airplaneClass;
    }

    // not finished
    @Override
    public void findDuration() {
        ;
    }
}