package adelina.luxtravel.domain.transport;

import adelina.luxtravel.domain.City;
import adelina.luxtravel.exception.FailedInitializationException;
import lombok.Getter;

import java.time.LocalDate;

@Getter
abstract public class Vehicle {
    private LocalDate releaseDate;
    private String brandName;

    public Vehicle(LocalDate releaseDate, String brandName) {
        initializeFields(releaseDate, brandName);
    }

    public abstract void findDuration(City from, City to);

    private void initializeFields(LocalDate releaseDate, java.lang.String brandName) {
        if (releaseDate == null || releaseDate.isAfter(LocalDate.now())) {
            throw new FailedInitializationException("Invalid release date");
        }
        if (brandName == null || brandName.isEmpty()) {
            throw new FailedInitializationException("Invalid brand name");
        }
        this.releaseDate = releaseDate;
        this.brandName = new java.lang.String(brandName);
    }
}