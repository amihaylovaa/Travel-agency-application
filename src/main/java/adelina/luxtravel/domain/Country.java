package adelina.luxtravel.domain;

import adelina.luxtravel.exception.FailedInitializationException;

import java.util.ArrayList;
import java.util.List;

public class Country {

    private String name;
    private List<City> cities;

    public Country(String name, List<City> cities) {
        initializeFields(name, cities);
    }

    private void initializeFields(String name, List<City> cities) {
        if (name == null || name.isEmpty()) {
            throw new FailedInitializationException("Invalid country name");
        }
        if (cities == null || cities.contains(null)) {
            throw new FailedInitializationException("Invalid cities");
        }
        this.name = name;
        this.cities = new ArrayList(cities);
    }
}