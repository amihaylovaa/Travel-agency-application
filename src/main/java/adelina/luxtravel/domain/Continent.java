package adelina.luxtravel.domain;

import adelina.luxtravel.exception.FailedInitializationException;
import lombok.Getter;
import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "continent")
@Getter
public class Continent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, unique = true, length = 5)
    ContinentList continentName;
    @OneToMany(mappedBy = "continent",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    List<Country> countries;

    public Continent(ContinentList continent, List<Country> countries) {
        initializeFields(continent, countries);
    }

    public Continent(Continent continent) {
        id = continent.id;
        continentName = continent.continentName;
        countries = new ArrayList<>(continent.countries);
    }

    private void initializeFields(ContinentList continentName, List<Country> countries) {
        if (countries == null || countries.contains(null)) {
            throw new FailedInitializationException("Invalid list of countries");
        }
        if (continentName == null) {
            throw new FailedInitializationException("Continent is not set");
        }
        this.continentName = continentName;
        this.countries = new ArrayList<>(countries);
    }
}