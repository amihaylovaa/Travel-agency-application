package adelina.luxtravel.domain;

import adelina.luxtravel.exception.FailedInitializationException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "country")
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "name", length = 64, nullable = false)
    private String name;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "country_id", referencedColumnName = "id")
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
        this.cities = new ArrayList<>(cities);
    }
}