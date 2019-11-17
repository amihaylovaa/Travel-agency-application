package adelina.luxtravel.domain;

import adelina.luxtravel.exception.FailedInitializationException;
import lombok.Getter;

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
    ContinentList continent;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "country_id", referencedColumnName = "id")
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