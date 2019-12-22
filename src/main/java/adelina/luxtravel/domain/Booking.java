package adelina.luxtravel.domain;

import adelina.luxtravel.exception.FailedInitializationException;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "booking")
@Getter
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull(message = "User can not be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "Traveling data can not be null")
    @JoinColumn(name = "travel_data_id")
    private TravelingData travelingData;
    @Size(min = 1, message = "Reserved tickets count can not be less than one")
    @Column(name = "reserved_tickets_count", nullable = false)
    private int reservedTicketsCount;

    public Booking(Booking booking) {
        this(booking.id, booking.travelingData, booking.user, booking.reservedTicketsCount);
    }

    public Booking(long id, TravelingData travelingData, User user, int reservedTicketsCount) {
        this(travelingData, user, reservedTicketsCount);
        this.id = id;
    }

    public Booking(TravelingData travelingData, User user, int reservedTicketsCount) {
        this.user = user;
        this.travelingData = travelingData;
        this.reservedTicketsCount = reservedTicketsCount;
    }
}