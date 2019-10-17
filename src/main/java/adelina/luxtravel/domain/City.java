package adelina.luxtravel.domain;

import adelina.luxtravel.exception.FailedInitializationException;
import lombok.Getter;

@Getter
public class City {
    private String name;
    private double distance;

    public City(String name, double distance) {
        initializeFields(name, distance);
    }

    public City(String name) {
        initializeName(name);
    }

    private void initializeFields(String name, double distance) {
        initializeName(name);

        if (distance <= 0.00) {
            throw new FailedInitializationException("Invalid distance");
        }
        this.name = name;
        this.distance = distance;
    }

    private void initializeName(String name) {
        if (name == null || name.isEmpty()) {
            throw new FailedInitializationException("Invalid city name");
        }
        this.name = name;
    }
}