package adelina.luxtravel.dto;

import adelina.luxtravel.domain.transport.TransportClass;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BusDTO extends TransportDTO {

    public BusDTO(long id, TransportClass transportClass) {
        super(id, transportClass);
    }

    public BusDTO(TransportClass transportClass) {
        super(transportClass);
    }
}
