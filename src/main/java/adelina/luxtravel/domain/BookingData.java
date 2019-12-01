package adelina.luxtravel.domain;

import adelina.luxtravel.domain.transport.Transport;
import adelina.luxtravel.domain.wrapper.Date;
import adelina.luxtravel.domain.wrapper.SourceDestination;
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
    @Embedded
    SourceDestination sourceDestination;
    @Embedded
    Date date;
    @OneToOne
    @JoinColumn(name = "transport_id")
    private Transport transport;

    public BookingData(BookingData bookingData) {
        this(bookingData.id, bookingData.sourceDestination, bookingData.transport, bookingData.date);
    }

    public BookingData(long id, SourceDestination sourceDestination, Transport transport, Date date) {
        this(transport, sourceDestination, date);
        this.id = id;
    }

    public BookingData(Transport transport, SourceDestination sourceDestination, Date date) {
        initializeFields(transport, sourceDestination, date);
    }

    private void initializeFields(Transport transport, SourceDestination sourceDestination, Date date) {
        if (transport == null) {
            throw new FailedInitializationException("Invalid transport");
        } else if (date == null) {
            throw new FailedInitializationException("Invalid dates");
        } else if (sourceDestination == null) {
            throw new FailedInitializationException("Invalid source or destination");
        } else {
            this.date = date;
            this.sourceDestination = sourceDestination;
            this.transport = transport;
        }
    }
}