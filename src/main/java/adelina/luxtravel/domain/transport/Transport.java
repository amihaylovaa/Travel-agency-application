package adelina.luxtravel.domain.transport;

import adelina.luxtravel.domain.TravelingPoint;
import adelina.luxtravel.exception.FailedInitializationException;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "transport")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
abstract public class Transport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "class", nullable = false, unique = true, length = 12)
    TransportClass transportClass;

    public Transport(TransportClass transportClass) {
        initializeFields(transportClass);
    }

    public Transport(Transport transport) {
        this(transport.id, transport.transportClass);
    }

    public Transport(long id, TransportClass transportClass) {
        this(transportClass);
        this.id = id;
    }

    public abstract LocalTime calculateDuration(TravelingPoint departurePoint, TravelingPoint destinationPoint);

    public LocalTime parseToLocalTime(Double duration) {
        String durationString = duration.toString().replace('.', ':');

        return LocalTime.parse(durationString, DateTimeFormatter.ofPattern("HH:mm"));
    }

    private void initializeFields(TransportClass transportClass) {
        if (transportClass == null) {
            throw new FailedInitializationException("Invalid vehicle class");
        }
        this.transportClass = transportClass;
    }
}