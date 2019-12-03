package adelina.luxtravel.domain;

import adelina.luxtravel.domain.transport.Transport;
import adelina.luxtravel.exception.FailedInitializationException;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "booking")
@Getter
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @OneToOne
    @JoinColumn(name = "booking_data_id")
    private BookingData bookingData;
    @Column(name = "price", nullable = false, precision = 6, scale = 2)
    private double price;
    @Column(name = "count_available_tickets", nullable = false)
    private int countAvailableTickets;

    public Booking(double price, BookingData bookingData, User user, int countAvailableTickets) {
        initializeFields(price, bookingData, user, countAvailableTickets);
    }

    public Booking(long id, double price, BookingData bookingData, User user, int countAvailableTickets) {
        this(price, bookingData, user, countAvailableTickets);
        this.id = id;
    }

    public Booking(Booking booking) {
        this(booking.id, booking.price, booking.bookingData, booking.user, booking.countAvailableTickets);
    }

    private void initializeFields(double price, BookingData bookingData, User user, int countAvailableTickets) {
        if (bookingData == null) {
            throw new FailedInitializationException("Null booking date");
        } else if (user == null) {
            throw new FailedInitializationException("Invalid user");
        } else {
            this.user = user;
            this.bookingData = bookingData;
            this.price = price;
            this.countAvailableTickets = countAvailableTickets;
        }
    }
}