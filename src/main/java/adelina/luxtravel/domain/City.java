package adelina.luxtravel.domain;

import adelina.luxtravel.exception.FailedInitializationException;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "city")
@Getter
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "name", length = 128, nullable = false)
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country;

    public City(String name) {
        initializeName(name);
    }

    public City(City city) {
        id = city.id;
        name = city.name;
        country = city.country;
    }

    private void initializeName(String name) {
        if (name == null || name.isEmpty()) {
            throw new FailedInitializationException("Invalid city name");
        }
        this.name = name;
    }
}