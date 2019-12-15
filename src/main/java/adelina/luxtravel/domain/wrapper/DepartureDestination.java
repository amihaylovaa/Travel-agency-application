package adelina.luxtravel.domain.wrapper;

import adelina.luxtravel.domain.TravelingPoint;
import adelina.luxtravel.exception.FailedInitializationException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@EqualsAndHashCode
@Getter
@Embeddable
public class DepartureDestination {
    @OneToOne
    @JoinColumn(name = "departure_point_id")
    private TravelingPoint departurePoint;
    @OneToOne
    @JoinColumn(name = "destination_point_id")
    private TravelingPoint destinationPoint;

    public DepartureDestination(TravelingPoint departurePoint, TravelingPoint destinationPoint) {
        setStartingEndingPoint(departurePoint, destinationPoint);
    }

    private void setStartingEndingPoint(TravelingPoint departurePoint, TravelingPoint destinationPoint) {
        if (departurePoint == null || destinationPoint == null || departurePoint.equals(destinationPoint)) {
            throw new FailedInitializationException("Invalid source or destination");
        }
        this.departurePoint = departurePoint;
        this.destinationPoint = destinationPoint;
    }
}