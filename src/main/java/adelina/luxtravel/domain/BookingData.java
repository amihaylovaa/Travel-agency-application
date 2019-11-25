package adelina.luxtravel.domain;

import adelina.luxtravel.domain.transport.Transport;
import adelina.luxtravel.domain.wrapper.Date;
import adelina.luxtravel.exception.FailedInitializationException;
import lombok.Getter;

import javax.persistence.*;

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
    @Embedded
    Date date;

    public BookingData(Transport transport, TravelingPoint source, TravelingPoint destination, Date date) {
        setSourceDestination(source, destination);
        setTransport(transport);
        setDate(date);
    }

    public BookingData(BookingData bookingData) {
        this(bookingData.id, bookingData.source, bookingData.destination, bookingData.transport, bookingData.date);
    }

    public BookingData(long id, TravelingPoint source, TravelingPoint destination, Transport transport, Date date) {
        this(transport, source, destination, date);
        this.id = id;
    }

    private void setTransport(Transport transport) {
        if (transport == null) {
            throw new FailedInitializationException("Invalid vehicle");
        }
        this.transport = transport;
    }

    private void setSourceDestination(TravelingPoint source, TravelingPoint destination) {
        if (source == null || destination == null || source == destination) {
            throw new FailedInitializationException("Invalid source or destination");
        }
        this.source = source;
        this.destination = destination;
    }

    private void setDate(Date date) {
        if (date == null) {
            throw new FailedInitializationException("Invalid dates");
        }
        this.date = date;
    }
}