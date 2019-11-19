package adelina.luxtravel.wrapper;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Embeddable
public class SourceDestinationId implements Serializable {
    @Column(name = "source_id", nullable = false)
    private long sourceId;
    @Column(name = "destination_id", nullable = false)
    private long destinationId;
}