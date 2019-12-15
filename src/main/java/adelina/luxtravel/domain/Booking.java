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
    @Column(name = "count_tickets", nullable = false)
    private int ticketsCount;

    public Booking(BookingData bookingData, User user, int ticketsCount) {
        initializeFields(bookingData, user, ticketsCount);
    }

    public Booking(long id, BookingData bookingData, User user, int ticketsCount) {
        this(bookingData, user, ticketsCount);
        this.id = id;
    }

    public Booking(Booking booking) {
        this(booking.id, booking.bookingData, booking.user, booking.ticketsCount);
    }

    private void initializeFields(BookingData bookingData, User user, int countTickets) {
        if (bookingData == null) {
            throw new FailedInitializationException("Invalid booking date");
        } else if (user == null) {
            throw new FailedInitializationException("Invalid user");
        } else {
            this.user = user;
            this.bookingData = bookingData;
            this.ticketsCount = countTickets;
        }
    }
}