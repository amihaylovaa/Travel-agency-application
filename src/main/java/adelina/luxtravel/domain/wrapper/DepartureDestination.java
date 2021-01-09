package adelina.luxtravel.domain.wrapper;

import adelina.luxtravel.domain.TravelingPoint;
import adelina.luxtravel.exception.FailedInitializationException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * Wrapper class representing departure and destination points of an excursion
 */
@EqualsAndHashCode
@NoArgsConstructor
@Getter
@Embeddable
public class DepartureDestination {
    @NotNull(message = "Departure can not be null")
    @ManyToOne
    @JoinColumn(name = "departure_point_id")
    private TravelingPoint departurePoint;
    @NotNull(message = "Destination can not be null")
    @ManyToOne
    @JoinColumn(name = "destination_point_id")
    private TravelingPoint destinationPoint;

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