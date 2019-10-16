package adelina.luxtravel.domain;

import adelina.luxtravel.exception.FailedInitializationException;

import java.util.ArrayList;
import java.util.List;

public class Continent {
    ContinentList continent;
    List<Country> countries;

    public Continent(ContinentList continent, List<Country> countries) {
        initializeFields(continent, countries);
    }

    private void initializeFields(ContinentList continent, List<Country> countries) {
        if (countries == null || countries.contains(null)) {
            throw new FailedInitializationException("Invalid list of countries");
        }
        if (continent == null) {
            throw new FailedInitializationException("Continent is not set");
        }
        this.continent = continent;
        this.countries = new ArrayList<>(countries);
    }
}