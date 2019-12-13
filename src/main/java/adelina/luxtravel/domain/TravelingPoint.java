package adelina.luxtravel.domain;

import adelina.luxtravel.exception.FailedInitializationException;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;

import static adelina.luxtravel.utility.Constants.NINETY_DEGREES;
import static adelina.luxtravel.utility.Constants.NINETY_DEGREES_NEGATIVE;

@Entity
@Table(name = "traveling_point")
@Getter
@EqualsAndHashCode
public class TravelingPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name", nullable = false, unique = true, length = 64)
    private String name;
    @Column(name = "longitude", nullable = false, precision = 10, scale = 2)
    private double longitude;
    @Column(name = "latitude", nullable = false, precision = 10, scale = 2)
    private double latitude;

    public TravelingPoint(String name, double longitude, double latitude) {
        initializeFields(name, longitude, latitude);
    }

    public TravelingPoint(long id, String name, double longitude, double latitude) {
        this(name, longitude, latitude);
        this.id = id;
    }

    public TravelingPoint(TravelingPoint travelingPoint) {
        this(travelingPoint.id, travelingPoint.name,
                travelingPoint.longitude, travelingPoint.latitude);
    }

    private void initializeFields(String name, double longitude, double latitude) {
        if (StringUtils.isEmpty(name)) {
            throw new FailedInitializationException("Invalid name");
        }
        if ((longitude > NINETY_DEGREES || longitude < NINETY_DEGREES_NEGATIVE) &&
                (latitude > NINETY_DEGREES || latitude < NINETY_DEGREES_NEGATIVE)) {
            throw new FailedInitializationException("Invalid coordinates");
        }
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}