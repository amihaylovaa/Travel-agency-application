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
    private City citySource;
    @OneToOne
    @JoinColumn(name = "destination_id")
    private City cityDestination;
    @OneToOne
    @JoinColumn(name = "transport_id")
    private Transport transport;
    @Column(name = "fromDate", nullable = false)
    private LocalDate from;
    @Column(name = "toDate", nullable = false)
    private LocalDate to;

    public BookingData(LocalDate from, LocalDate to, Transport transport, City citySource, City cityDestination) {
        setBookingDates(from, to);
        setSourceDestination(citySource, cityDestination);
        setTransport(transport);
    }

    public BookingData(BookingData bookingData) {
        from = bookingData.from;
        to = bookingData.to;
        transport = bookingData.transport;
        citySource = bookingData.citySource;
        cityDestination = bookingData.cityDestination;
    }

    private void setTransport(Transport transport) {
        if (transport == null) {
            throw new FailedInitializationException("Invalid vehicle");
        }
        this.transport = transport;
    }

    private void setSourceDestination(City citySource, City cityDestination) {
        if (citySource == null || cityDestination == null) {
            throw new FailedInitializationException("Invalid source or destination is not set");
        }
        this.citySource = cityDestination;
        this.cityDestination = cityDestination;
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