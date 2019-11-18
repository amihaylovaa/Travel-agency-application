package adelina.luxtravel.domain.transport;

import adelina.luxtravel.domain.City;
import adelina.luxtravel.exception.FailedInitializationException;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "vehicle")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
abstract public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, unique = true, length = 12)
    VehicleClass vehicleClass;

    public Vehicle(VehicleClass vehicleClass) {
        initializeFields(vehicleClass);
    }

    private void initializeFields(VehicleClass vehicleClass) {
        if (vehicleClass == null) {
            throw new FailedInitializationException("Invalid vehicle class");
        }
        this.vehicleClass = vehicleClass;
    }

    public abstract void calculateDuration(City to);

    public LocalTime parseToLocalTime(Double duration) {
        String durationString = duration.toString().replace('.', ':');

        return LocalTime.parse(durationString, DateTimeFormatter.ofPattern("HH:mm"));
    }
}