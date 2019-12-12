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
    StartingEndingPoint startingEndingPoint;
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
        this(bookingData.id, bookingData.startingEndingPoint,
                bookingData.transport, bookingData.date,
                bookingData.availableTicketsCount);
    }

    public BookingData(long id, StartingEndingPoint startingEndingPoint,
                       Transport transport, Date date, int availableTicketsCount) {
        this(transport, startingEndingPoint, date, availableTicketsCount);
        this.id = id;
    }

    public BookingData(Transport transport, StartingEndingPoint startingEndingPoint,
                       Date date, int availableTicketsCount) {
        initializeFields(transport, startingEndingPoint, date, availableTicketsCount);
    }

    private void initializeFields(Transport transport, StartingEndingPoint startingEndingPoint,
                                  Date date, int availableTicketsCount) {
        if (transport == null) {
            throw new FailedInitializationException("Invalid transport");
        } else if (date == null) {
            throw new FailedInitializationException("Invalid dates");
        } else if (startingEndingPoint == null) {
            throw new FailedInitializationException("Invalid source or destination");
        } else {
            this.date = date;
            this.startingEndingPoint = startingEndingPoint;
            this.transport = transport;
            this.availableTicketsCount = availableTicketsCount;
            setPrice();
        }
    }

    private void setPrice() {
        TransportClass transportClass = transport.getTransportClass();
        double priceCoefficient = transportClass.getPriceCoefficient();
        TravelingPoint source = startingEndingPoint.getStartingPoint();
        TravelingPoint destination = startingEndingPoint.getTargetPoint();

        LocalTime localTime = transport.calculateDuration(source, destination);

        price = ((localTime.getHour() * MINUTE + localTime.getMinute()) / priceCoefficient) * TEN_PERCENT;
    }
}