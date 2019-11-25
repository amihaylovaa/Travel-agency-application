package adelina.luxtravel.domain.wrapper;

import adelina.luxtravel.domain.TravelingPoint;
import adelina.luxtravel.exception.FailedInitializationException;
import lombok.Getter;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Getter
@Embeddable
public class SourceDestination {
    @OneToOne
    @JoinColumn(name = "source_id")
    private TravelingPoint source;
    @OneToOne
    @JoinColumn(name = "destination_id")
    private TravelingPoint destination;

    private void setSourceDestination(TravelingPoint source, TravelingPoint destination) {
        if (source == null || destination == null || source == destination) {
            throw new FailedInitializationException("Invalid source or destination");
        }
        this.source = source;
        this.destination = destination;
    }
}
