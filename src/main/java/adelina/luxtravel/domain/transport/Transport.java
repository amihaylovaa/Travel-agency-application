package adelina.luxtravel.domain.transport;

import adelina.luxtravel.domain.TravelingPoint;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "transport")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
public abstract class Transport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected long id;
    @NotNull(message = "Transport class can not be null")
    @Enumerated(EnumType.STRING)
    @Column(name = "class", nullable = false, unique = true, length = 12)
    TransportClass transportClass;

    public Transport(TransportClass transportClass) {
        this.transportClass = transportClass;
    }

    public Transport(Transport transport) {
        this(transport.id, transport.transportClass);
    }

    public Transport(long id, TransportClass transportClass) {
        this(transportClass);
        this.id = id;
    }

    public LocalTime parseToLocalTime(Double duration) {
        String durationString = duration.toString().replace('.', ':');

        return LocalTime.parse(durationString, DateTimeFormatter.ofPattern("HH:mm"));
    }

    public abstract LocalTime calculateDuration(TravelingPoint departurePoint, TravelingPoint destinationPoint);
}