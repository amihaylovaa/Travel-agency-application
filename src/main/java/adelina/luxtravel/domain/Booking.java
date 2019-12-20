package adelina.luxtravel.domain;

import adelina.luxtravel.exception.FailedInitializationException;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_data_id")
    private TravelingData travelingData;
    @Column(name = "reserved_tickets_count", nullable = false)
    private int reservedTicketsCount;


    public Booking(TravelingData travelingData, User user, int reservedTicketsCount) {
        initializeFields(travelingData, user, reservedTicketsCount);
    }

    public Booking(long id, TravelingData travelingData, User user, int reservedTicketsCount) {
        this(travelingData, user, reservedTicketsCount);
        this.id = id;
    }

    public Booking(Booking booking) {
        this(booking.id, booking.travelingData, booking.user, booking.reservedTicketsCount);
    }

    private void initializeFields(TravelingData travelingData, User user, int reservedTicketsCount) {
        if (travelingData == null) {
            throw new FailedInitializationException("Invalid booking date");
        } else if (user == null) {
            throw new FailedInitializationException("Invalid user");
        } else {
            this.user = user;
            this.travelingData = travelingData;
            this.reservedTicketsCount = reservedTicketsCount;
        }
    }
}