package adelina.luxtravel.dto;

import adelina.luxtravel.domain.Booking;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TravelingDataDTO {
    private long id;
    DepartureDestinationDTO departureDestinationDTO;
    DateDTO dateDTO;
    private TransportDTO transportDTO;
    private int availableTicketsCount;
    private double price;
    private List<Booking> bookings;
}
