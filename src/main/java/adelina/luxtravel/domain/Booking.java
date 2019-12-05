package adelina.luxtravel.domain;

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
    @Column(name = "count_tickets", nullable = false)
    private int countTickets;

    public Booking(double price, BookingData bookingData, User user, int countTickets) {
        initializeFields(price, bookingData, user, countTickets);
    }

    public Booking(long id, double price, BookingData bookingData, User user, int countTickets) {
        this(price, bookingData, user, countTickets);
        this.id = id;
    }

    public Booking(Booking booking) {
        this(booking.id, booking.price, booking.bookingData, booking.user, booking.countTickets);
    }

    private void initializeFields(double price, BookingData bookingData, User user, int countTickets) {
        if (bookingData == null) {
            throw new FailedInitializationException("Null booking date");
        } else if (user == null) {
            throw new FailedInitializationException("Invalid user");
        } else {
            this.user = user;
            this.bookingData = bookingData;
            this.price = price;
            this.countTickets = countTickets;
        }
    }
}