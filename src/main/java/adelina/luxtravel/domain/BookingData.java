package adelina.luxtravel.domain;

import adelina.luxtravel.domain.transport.Transport;
import adelina.luxtravel.domain.transport.TransportClass;

import static adelina.luxtravel.utility.Constants.MINUTE;
import static adelina.luxtravel.utility.Constants.TEN_PERCENT;

import adelina.luxtravel.domain.wrapper.*;
import adelina.luxtravel.exception.FailedInitializationException;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalTime;

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
    private int availableTicketsCount;
    @Column(name = "price", nullable = false, precision = 6, scale = 2)
    private double price;

    public BookingData(BookingData bookingData) {
        this(bookingData.id, bookingData.sourceDestination,
                bookingData.transport, bookingData.date,
                bookingData.availableTicketsCount);
    }

    public BookingData(long id, SourceDestination sourceDestination,
                       Transport transport, Date date, int availableTicketsCount) {
        this(transport, sourceDestination, date, availableTicketsCount);
        this.id = id;
    }

    public BookingData(Transport transport, SourceDestination sourceDestination,
                       Date date, int availableTicketsCount) {
        initializeFields(transport, sourceDestination, date, availableTicketsCount);
    }

    private void initializeFields(Transport transport, SourceDestination sourceDestination,
                                  Date date, int availableTicketsCount) {
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
            this.availableTicketsCount = availableTicketsCount;
            setPrice();
        }
    }

    private void setPrice() {
        TransportClass transportClass = transport.getTransportClass();
        double priceCoefficient = transportClass.getPriceCoefficient();
        TravelingPoint source = sourceDestination.getSource();
        TravelingPoint destination = sourceDestination.getDestination();

        LocalTime localTime = transport.calculateDuration(source, destination);

        price = ((localTime.getHour() * MINUTE + localTime.getMinute()) / priceCoefficient) * TEN_PERCENT;
    }
}