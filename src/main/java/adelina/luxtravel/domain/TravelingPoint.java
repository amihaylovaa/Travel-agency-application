package adelina.luxtravel.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;

/**
 * Represents a traveling point (geographic point)
 */
@Entity
@Table(name = "traveling_points")
@Getter
@NoArgsConstructor
public class TravelingPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotEmpty(message = "Name can not be null or empty")
    @Length(min = 3, message = "Traveling point name can not be less than 3 characters")
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @DecimalMin(value = "-90.0", message = "Latitude can not be lower than -90 degrees")
    @DecimalMax(value = "90.0", message = "Latitude can not be higher than 90 degrees")
    @Column(name = "latitude", nullable = false)
    private double latitude;
    @DecimalMin(value = "-180.0", message = "Longitude can not be lower than -180 degrees")
    @DecimalMax(value = "180.0", message = "Longitude can not be higher than 180 degrees")
    @Column(name = "longitude", nullable = false)
    private double longitude;

    public TravelingPoint(long id, String name, double longitude, double latitude) {
        this(name, longitude, latitude);
        this.id = id;
    }

    public TravelingPoint(String name, double longitude, double latitude) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}