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
    @Column(name = "count_available_tickets", nullable = false)
    private int countAvailableTickets;

    public BookingData(BookingData bookingData) {
        this(bookingData.id, bookingData.sourceDestination,
                bookingData.transport, bookingData.date, bookingData.countAvailableTickets);
    }

    public BookingData(long id, SourceDestination sourceDestination,
                       Transport transport, Date date, int countAvailableTickets) {
        this(transport, sourceDestination, date, countAvailableTickets);
        this.id = id;
    }

    public BookingData(Transport transport, SourceDestination sourceDestination,
                       Date date, int countAvailableTickets) {
        initializeFields(transport, sourceDestination, date, countAvailableTickets);
    }

    private void initializeFields(Transport transport, SourceDestination sourceDestination,
                                  Date date, int countAvailableTickets) {
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
            this.countAvailableTickets = countAvailableTickets;
        }
    }
}