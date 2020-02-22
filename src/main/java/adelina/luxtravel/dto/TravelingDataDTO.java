package adelina.luxtravel.dto;

import adelina.luxtravel.domain.Booking;
import adelina.luxtravel.domain.transport.Transport;
import adelina.luxtravel.domain.wrapper.Date;
import adelina.luxtravel.domain.wrapper.DepartureDestination;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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

    public TravelingDataDTO(long id, DepartureDestinationDTO departureDestinationDTO, DateDTO dateDTO, TransportDTO transportDTO, int availableTicketsCount, double price) {
        this.id = id;
        this.departureDestinationDTO = departureDestinationDTO;
        this.dateDTO = dateDTO;
        this.transportDTO = transportDTO;
        this.availableTicketsCount = availableTicketsCount;
        this.price = price;
        bookings = new ArrayList<>();
    }
}
