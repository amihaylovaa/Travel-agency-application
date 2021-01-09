package adelina.luxtravel.domain.wrapper;


import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Represents composite primary key
 */
@NoArgsConstructor
@EqualsAndHashCode
public class ExcursionTransportId implements Serializable {
    private long transport;
    private long excursion;
}
