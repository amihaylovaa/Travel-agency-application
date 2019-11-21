package adelina.luxtravel.domain.wrapper;

import adelina.luxtravel.domain.City;
import adelina.luxtravel.domain.transport.Transport;
import adelina.luxtravel.exception.FailedInitializationException;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "booking_data")
@Getter
public class BookingData {
    @EmbeddedId
    SourceDestinationId sourceDestinationId;
    @Column(name = "fromDate", nullable = false)
    private LocalDate from;
    @Column(name = "toDate", nullable = false)
    private LocalDate to;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Transport transport;
    @MapsId("city")
    @ManyToOne
    City source;
    @MapsId("city")
    @ManyToOne
    City destination;

    public BookingData(LocalDate from, LocalDate to, Transport transport, SourceDestinationId sourceDestinationId) {
        setBookingDates(from, to);
        setTransport(transport);
        setSourceDestinationId(sourceDestinationId);
    }

    public BookingData(BookingData bookingData) {
        from = bookingData.from;
        to = bookingData.to;
        transport = bookingData.transport;
        sourceDestinationId = bookingData.sourceDestinationId;
    }

    private void setSourceDestinationId(SourceDestinationId sourceDestinationId) {
        if (sourceDestinationId == null) {
            throw new FailedInitializationException("Starting or ending point is not set");
        }
        this.sourceDestinationId = sourceDestinationId;
    }

    private void setTransport(Transport transport) {
        if (transport == null) {
            throw new FailedInitializationException("Invalid vehicle");
        }
        this.transport = transport;
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