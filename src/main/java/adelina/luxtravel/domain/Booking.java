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
    private List<TravelData> travelData;
    @Column(name = "reserved_tickets_count", nullable = false)
    private int reservedTicketsCount;


    public Booking(List<TravelData> travelData, User user, int reservedTicketsCount) {
        initializeFields(travelData, user, reservedTicketsCount);
    }

    public Booking(long id, List<TravelData> travelData, User user, int reservedTicketsCount) {
        this(travelData, user, reservedTicketsCount);
        this.id = id;
    }

    public Booking(Booking booking) {
        this(booking.id, booking.travelData, booking.user, booking.reservedTicketsCount);
    }

    private void initializeFields(List<TravelData> travelData, User user, int reservedTicketsCount) {
        if (travelData == null) {
            throw new FailedInitializationException("Invalid booking date");
        } else if (user == null) {
            throw new FailedInitializationException("Invalid user");
        } else {
            this.user = user;
            this.travelData = new ArrayList<>(travelData);
            this.reservedTicketsCount = reservedTicketsCount;
        }
    }
}