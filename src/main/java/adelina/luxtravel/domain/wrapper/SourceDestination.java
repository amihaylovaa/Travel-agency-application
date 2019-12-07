package adelina.luxtravel.domain.wrapper;

import adelina.luxtravel.domain.TravelingPoint;
import adelina.luxtravel.exception.FailedInitializationException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@EqualsAndHashCode
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
        if (source == null || destination == null || source.equals(destination)) {
            throw new FailedInitializationException("Invalid source or destination");
        }
        this.source = source;
        this.destination = destination;
    }
}