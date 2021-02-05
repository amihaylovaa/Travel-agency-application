package adelina.luxtravel.domain;

import adelina.luxtravel.domain.transport.Transport;
import adelina.luxtravel.enumeration.TransportClass;

import adelina.luxtravel.domain.wrapper.*;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

/**
 * Represents an excursion
 */
@Entity
@Table(name = "excursions")
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class Excursion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private long id;
    @NotNull(message = "Departure point and destination point can not be null")
    @Embedded
    private DepartureDestination departureDestination;
    @NotNull(message = "Dates can not be null")
    @Embedded
    private Date date;
    @OneToMany(mappedBy = "excursion")
    @JsonManagedReference(value = "excursion")
    private List<ExcursionTransport> excursionTransports;
    @Min(value = 1, message = "Count available tickets can not be less than one")
    @Column(name = "available_tickets_count", nullable = false)
    private int availableTicketsCount;
    @Transient
    private static final double MINUTE;
    @Transient
    private static final double PERCENT;

    static {
        MINUTE = 60.0;
        PERCENT = 0.05;
    }

    public Excursion(long id, DepartureDestination departureDestination, Transport transport, Date date, int availableTicketsCount) {
        this(departureDestination, date, availableTicketsCount);
        this.id = id;
        ExcursionTransport excursionTransport = new ExcursionTransport();
        excursionTransport.setTransport(transport);
        excursionTransports.add(excursionTransport);
    }

    public Excursion(long id, DepartureDestination departureDestination, Date date, List<ExcursionTransport> excursionTransports, int availableTicketsCount) {
        this.id = id;
        this.departureDestination = departureDestination;
        this.date = date;
        this.excursionTransports = excursionTransports;
        this.availableTicketsCount = availableTicketsCount;
    }

    public Excursion(DepartureDestination departureDestination, Date date, int availableTicketsCount) {
        this.departureDestination = departureDestination;
        this.date = date;
        this.availableTicketsCount = availableTicketsCount;
    }

    public double setPrice(Transport transport) {
        TransportClass transportClass = transport.getTransportClass();
        TravelingPoint departurePoint = departureDestination.getDeparturePoint();
        TravelingPoint destinationPoint = departureDestination.getDestinationPoint();
        double priceCoefficient = transportClass.getPriceCoefficient();

        LocalTime localTime = transport.calculateDuration(departurePoint, destinationPoint);

        return (((localTime.getHour() * MINUTE + localTime.getMinute()) / priceCoefficient) * PERCENT) * availableTicketsCount;
    }
}