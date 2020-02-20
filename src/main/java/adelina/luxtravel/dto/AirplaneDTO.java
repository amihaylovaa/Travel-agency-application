package adelina.luxtravel.dto;

import adelina.luxtravel.domain.transport.TransportClass;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AirplaneDTO extends TransportDTO {
    public AirplaneDTO(long id, TransportClass transportClass) {
        super(id, transportClass);
    }

    public AirplaneDTO(TransportClass transportClass) {
        super(transportClass);
    }
}
