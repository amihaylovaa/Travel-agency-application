package adelina.luxtravel.domain;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;

/**
 * Represents a  traveling point
 */

@Entity
@Table(name = "traveling_point")
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class TravelingPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotEmpty(message = "Traveling point name can not be null or empty")
    @Length(min = 3, max = 64, message = "Traveling point name can not be less than 5 or more than 60 characters")
    @Column(name = "name", nullable = false, unique = true, length = 64)
    private String name;
    @DecimalMin(value = "-90.0", message = "Longitude can not be lower than -90 degrees")
    @DecimalMax(value = "90.0", message = "Longitude can not be higher than 90 degrees")
    @Column(name = "longitude", nullable = false, precision = 10, scale = 2)
    private double longitude;
    @DecimalMin(value = "-90.0", message = "Latitude can not be lower than -90 degrees")
    @DecimalMax(value = "90.0", message = "Latitude can not be higher than 90 degrees")
    @Column(name = "latitude", nullable = false, precision = 10, scale = 2)
    private double latitude;

    /**
     * Constructor
     *
     * @param travelingPoint  object contains traveling point's data
     */
    public TravelingPoint(TravelingPoint travelingPoint) {
        this(travelingPoint.id, travelingPoint.name,
                travelingPoint.longitude, travelingPoint.latitude);
    }

    /**
     * Constructor
     *
     * @param id traveling point's id
     * @param name traveling point's name
     * @param longitude traveling point's longitude
     * @param latitude traveling point's latitude
     */
    public TravelingPoint(long id, String name, double longitude, double latitude) {
        this(name, longitude, latitude);
        this.id = id;
    }

    /**
     * Constructor
     *
     * @param name traveling point's name
     * @param longitude traveling point's longitude
     * @param latitude traveling point's latitude
     */
    public TravelingPoint(String name, double longitude, double latitude) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}