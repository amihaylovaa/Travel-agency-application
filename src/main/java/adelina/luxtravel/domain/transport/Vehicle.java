package adelina.luxtravel.domain.transport;

import adelina.luxtravel.domain.City;
import adelina.luxtravel.exception.FailedInitializationException;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Table(name = "vehicle")
@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
abstract public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected long id;
    @Column(name = "brand_name", unique = true, nullable = false, length = 32)
    protected String brandName;

    public Vehicle(String brandName) {
        initializeFields(brandName);
    }

    private void initializeFields(String brandName) {
        if (brandName == null || brandName.isEmpty()) {
            throw new FailedInitializationException("Invalid brand name");
        }
        this.brandName = new String(brandName);
    }

    public abstract void calculateDuration(City to);

    public LocalTime parseToLocalTime(Double duration) {
        String durationString = duration.toString().replace('.', ':');

        return LocalTime.parse(durationString, DateTimeFormatter.ofPattern("HH:mm"));
    }
}