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
public class StartingEndingPoint {
    @OneToOne
    @JoinColumn(name = "starting_point_id")
    private TravelingPoint startingPoint;
    @OneToOne
    @JoinColumn(name = "target_point_id")
    private TravelingPoint targetPoint;

    public StartingEndingPoint(TravelingPoint startingPoint, TravelingPoint targetPoint) {
        setStartingEndingPoint(startingPoint, targetPoint);
    }

    private void setStartingEndingPoint(TravelingPoint startingPoint, TravelingPoint targetPoint) {
        if (startingPoint == null || targetPoint == null || startingPoint.equals(targetPoint)) {
            throw new FailedInitializationException("Invalid source or destination");
        }
        this.startingPoint = startingPoint;
        this.targetPoint = targetPoint;
    }
}