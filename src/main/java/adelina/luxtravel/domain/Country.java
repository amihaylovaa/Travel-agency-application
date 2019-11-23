package adelina.luxtravel.domain;

import adelina.luxtravel.exception.FailedInitializationException;
import lombok.Getter;

import javax.persistence.*;
import javax.persistence.criteria.Fetch;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "country")
@Getter
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "name", length = 64, nullable = false)
    private String name;
    @OneToMany(mappedBy = "country",
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    private List<City> cities;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "continent_id")
    private Continent continent;

    public Country(String name, List<City> cities) {
        initializeFields(name, cities);
    }

    public Country(Country country) {
        id = country.id;
        name = country.name;
        cities = new ArrayList<>(country.cities);
        continent = country.continent;
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