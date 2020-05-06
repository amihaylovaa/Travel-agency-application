package adelina.luxtravel.domain.wrapper;

import adelina.luxtravel.domain.TravelingPoint;
import adelina.luxtravel.exception.FailedInitializationException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

/**
 * Wrapper class representing departure and destination traveling points
 */
@EqualsAndHashCode
@NoArgsConstructor
@Getter
@Embeddable
public class DepartureDestination {
    @NotNull(message = "Departure can not be null")
    @OneToOne
    @JoinColumn(name = "departure_point_id")
    private TravelingPoint departurePoint;
    @NotNull(message = "Destination can not be null")
    @OneToOne
    @JoinColumn(name = "destination_point_id")
    private TravelingPoint destinationPoint;

    /**
     * Constructor
     *
     * @param departurePoint object contains departure traveling point's data
     * @param destinationPoint object contains destination traveling point's data
     */
    public DepartureDestination(TravelingPoint departurePoint, TravelingPoint destinationPoint) {
        setStartingEndingPoint(departurePoint, destinationPoint);
    }

    private void setStartingEndingPoint(TravelingPoint departurePoint, TravelingPoint destinationPoint) {
        if (departurePoint.equals(destinationPoint)) {
            throw new FailedInitializationException("Departure point can not be the same as destination point");
        }
        this.departurePoint = departurePoint;
        this.destinationPoint = destinationPoint;
    }
}