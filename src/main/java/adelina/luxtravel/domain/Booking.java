package adelina.luxtravel.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "booking")
@Getter
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull(message = "User can not be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @NotNull(message = "Traveling data can not be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "traveling_data_id")
    private TravelingData travelingData;
    @Min(value = 1, message = "Reserved tickets count can not be less than one")
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