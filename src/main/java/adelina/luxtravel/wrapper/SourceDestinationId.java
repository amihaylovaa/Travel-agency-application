package adelina.luxtravel.wrapper;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class SourceDestinationId implements Serializable {
    @OneToOne(fetch = FetchType.LAZY)
    @Column(name = "source_id", nullable = false)
    private TravelPoint sourceId;
    @OneToOne(fetch = FetchType.LAZY)
    @Column(name = "destination_id", nullable = false)
    private TravelPoint destinationId;

    public SourceDestinationId(TravelPoint sourceId, TravelPoint destinationId) {
        this.sourceId = sourceId;
        this.destinationId = destinationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SourceDestinationId that = (SourceDestinationId) o;
        return Objects.equals(sourceId, that.sourceId) &&
                Objects.equals(destinationId, that.destinationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceId, destinationId);
    }
}