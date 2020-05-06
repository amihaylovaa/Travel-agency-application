package adelina.luxtravel.domain;

import adelina.luxtravel.domain.transport.Transport;
import adelina.luxtravel.domain.transport.TransportClass;

import static adelina.luxtravel.utility.Constants.MINUTE;
import static adelina.luxtravel.utility.Constants.PERCENT;

import adelina.luxtravel.domain.wrapper.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Entity
@Table(name = "traveling_data")
@Getter
@NoArgsConstructor
public class TravelingData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull(message = "Departure point and destination point can not be null")
    @Embedded
    DepartureDestination departureDestination;
    @NotNull(message = "Dates can not be null")
    @Embedded
    Date date;
    @NotNull(message = "Transport can not be null")
    @ManyToOne
    @JoinColumn(name = "transport_id")
    private Transport transport;
    @Min(value = 1, message = "Count available tickets must have at least one ticket")
    @Column(name = "available_tickets_count", nullable = false)
    private int availableTicketsCount;
    @DecimalMin(value = "5.0", message = "Price can not be less than 5.0 ")
    @Column(name = "price", nullable = false, precision = 6, scale = 2)
    private double price;

    public TravelingData(TravelingData travelingData) {
        id = travelingData.getId();
        availableTicketsCount = travelingData.getAvailableTicketsCount();
        date = travelingData.getDate();
        departureDestination = travelingData.getDepartureDestination();
        transport = travelingData.getTransport();
        setPrice();
    }

    public TravelingData(long id, DepartureDestination departureDestination, Transport transport,
                         Date date, int availableTicketsCount) {
        this(transport, departureDestination, date, availableTicketsCount);
        this.id = id;
    }

    public TravelingData(Transport transport, DepartureDestination departureDestination,
                         Date date, int availableTicketsCount) {
        this.transport = transport;
        this.departureDestination = departureDestination;
        this.date = date;
        this.availableTicketsCount = availableTicketsCount;
        setPrice();
    }

    private void setPrice() {
        TransportClass transportClass = transport.getTransportClass();
        double priceCoefficient = transportClass.getPriceCoefficient();
        TravelingPoint departurePoint = departureDestination.getDeparturePoint();
        TravelingPoint destinationPoint = departureDestination.getDestinationPoint();

        LocalTime localTime = transport.calculateDuration(departurePoint, destinationPoint);

        price = ((localTime.getHour() * MINUTE + localTime.getMinute()) / priceCoefficient) * PERCENT;
    }
}