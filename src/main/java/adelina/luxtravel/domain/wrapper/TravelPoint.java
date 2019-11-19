package adelina.luxtravel.domain.wrapper;

import adelina.luxtravel.domain.City;
import adelina.luxtravel.domain.Continent;
import adelina.luxtravel.domain.Country;
import adelina.luxtravel.exception.FailedInitializationException;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "travel_point")
@Getter
public class TravelPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @OneToOne(cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinColumn(name = "continent_id")
    private Continent continent;
    @OneToOne(cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinColumn(name = "country_id")
    private Country country;
    @OneToOne(cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinColumn(name = "city_id")
    private City city;

    public TravelPoint(Continent continent, Country country, City city) {
        initializeFields(continent, country, city);
    }

    private boolean fieldsCompatible(Continent continent, Country country, City city) {
        List<Country> countries = continent.getCountries();
        List<City> cities = country.getCities();

        return (countries.contains(country) && cities.contains(city));
    }

    private void initializeFields(Continent continent, Country country, City city) {
        try {
            if (!fieldsCompatible(continent, country, city)) {
                throw new FailedInitializationException("Incompatible parameters");
            }
        } catch (NullPointerException npe) {
            throw new FailedInitializationException("Null parameters");
        }
        this.continent = continent;
        this.country = country;
        this.city = city;
    }
}