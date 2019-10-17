package adelina.luxtravel.domain.transport;

import adelina.luxtravel.domain.City;

import java.time.LocalDate;

public class Bus extends Vehicle {
    public Bus(LocalDate releaseDate, java.lang.String brandName) {
        super(releaseDate, brandName);
    }

    // not finished
    @Override
    public void findDuration(City from, City to) {
        ;
    }
}
