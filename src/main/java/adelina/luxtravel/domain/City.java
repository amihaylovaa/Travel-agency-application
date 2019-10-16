package adelina.luxtravel.domain;

import adelina.luxtravel.exception.FailedInitializationException;
import lombok.Getter;

@Getter
public class City {
    private String name;

    public City(String name) {
        initializeName(name);
    }

    private void initializeName(String name) {
        if (name == null || name.isEmpty()) {
            throw new FailedInitializationException("Invalid city name");
        }
        this.name = name;
    }
}