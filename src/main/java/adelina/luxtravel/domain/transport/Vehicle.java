package adelina.luxtravel.domain.transport;

import adelina.luxtravel.exception.FailedInitializationException;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class Vehicle {
    private LocalDate releaseDate;
    private String brandName;

    public Vehicle(LocalDate releaseDate, String brandName) {
        initializeFields(releaseDate, brandName);
    }

    private void initializeFields(LocalDate releaseDate, String brandName) {
        if (releaseDate == null || releaseDate.isAfter(LocalDate.now())) {
            throw new FailedInitializationException("Invalid release date");
        }
        if (brandName == null || brandName.isEmpty()) {
            throw new FailedInitializationException("Invalid brand name");
        }
        this.releaseDate = releaseDate;
        this.brandName = brandName;
    }
}