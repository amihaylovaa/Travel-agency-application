package adelina.luxtravel.dto;

import adelina.luxtravel.domain.TravelingPoint;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DepartureDestinationDTO {
        long id;
        private TravelingPoint departurePoint;
        private TravelingPoint destinationPoint;
}
