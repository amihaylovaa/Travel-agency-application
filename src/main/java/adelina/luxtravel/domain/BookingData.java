package adelina.luxtravel.domain;

import adelina.luxtravel.domain.transport.Transport;
import adelina.luxtravel.exception.FailedInitializationException;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "booking_data")
@Getter
public class BookingData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    @JoinColumn(name = "source_id")
    private TravelingPoint source;
    @OneToOne
    @JoinColumn(name = "destination_id")
    private TravelingPoint destination;
    @OneToOne
    @JoinColumn(name = "transport_id")
    private Transport transport;
    @Column(name = "from_date", nullable = false)
    private LocalDate from;
    @Column(name = "to_date", nullable = false)
    private LocalDate to;

    public BookingData(LocalDate from, LocalDate to, Transport transport, TravelingPoint source, TravelingPoint destination) {
        setBookingDates(from, to);
        setSourceDestination(source, destination);
        setTransport(transport);
    }

    public BookingData(BookingData bookingData) {
        id = bookingData.id;
        from = bookingData.from;
        to = bookingData.to;
        transport = bookingData.transport;
        source = bookingData.source;
        destination = bookingData.destination;
    }

    private void setTransport(Transport transport) {
        if (transport == null) {
            throw new FailedInitializationException("Invalid vehicle");
        }
        this.transport = transport;
    }

    private void setSourceDestination(TravelingPoint source, TravelingPoint destination) {
        if (source == null || destination == null) {
            throw new FailedInitializationException("Invalid source or destination is not set");
        }
        this.source = source;
        this.destination = destination;
    }

    private void setBookingDates(LocalDate from, LocalDate to) {
        try {
            if (from.isAfter(to) || from.isEqual(to) || from.isBefore(LocalDate.now())) {
                throw new FailedInitializationException("Invalid dates");
            }
        } catch (NullPointerException npe) {
            throw new FailedInitializationException("Null dates are forbidden");
        }
        this.from = from;
        this.to = to;
    }
}