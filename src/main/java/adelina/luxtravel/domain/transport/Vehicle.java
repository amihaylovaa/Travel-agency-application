package adelina.luxtravel.domain.transport;

import adelina.luxtravel.domain.City;
import adelina.luxtravel.exception.FailedInitializationException;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
abstract public class Vehicle {
    private LocalDate releaseDate;
    private String brandName;

    public Vehicle(LocalDate releaseDate, String brandName) {
        initializeFields(releaseDate, brandName);
    }

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

    public abstract LocalTime calculateDuration(City to);

    public LocalTime parseToLocalTime(Double duration) {
        String durationString = duration.toString().replace('.', ':');

        return LocalTime.parse(durationString, DateTimeFormatter.ofPattern("HH:mm"));
    }
}