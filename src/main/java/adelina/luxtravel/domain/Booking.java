package adelina.luxtravel.domain;

import adelina.luxtravel.domain.wrapper.ExcursionTransport;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Represents a booking for an excursion
 */
@Entity
@Table(name = "bookings")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull(message = "User can not be null")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @NotNull(message = "Excursion can not be null")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "excursion_id")
    @JoinColumn(name = "transport_id")
    private ExcursionTransport excursionTransport;
    @Column(name = "reservation_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime reservationDate;
    @Min(value = 1, message = "Reserved tickets count can not be less than one")
    @Column(name = "reserved_tickets_count", nullable = false)
    private int reservedTicketsCount;

    public Booking(long id, ExcursionTransport excursionTransport, User user, int reservedTicketsCount) {
        this(excursionTransport, user, reservedTicketsCount);
        this.id = id;
    }

    public Booking(ExcursionTransport excursionTransport, User user, int reservedTicketsCount) {
        this.user = user;
        this.excursionTransport = excursionTransport;
        this.reservedTicketsCount = reservedTicketsCount;
    }
}