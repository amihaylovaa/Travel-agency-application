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

        public DepartureDestinationDTO(TravelingPoint departurePoint, TravelingPoint destinationPoint) {
                this.departurePoint = departurePoint;
                this.destinationPoint = destinationPoint;
        }
}
