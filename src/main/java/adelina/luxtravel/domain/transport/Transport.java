package adelina.luxtravel.domain.transport;

import adelina.luxtravel.domain.TravelingPoint;

import adelina.luxtravel.enumeration.TransportClass;
import adelina.luxtravel.exception.FailedInitializationException;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.Locale;

/**
 * Abstract class that represents the means of transport
 */
@Entity
@Table(name = "transports")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Airplane.class, name = "Airplane"),
        @JsonSubTypes.Type(value = Bus.class, name = "Bus")
})
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "type")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public abstract class Transport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected long id;
    @NotNull(message = "Transport class can not be null")
    @Enumerated(EnumType.STRING)
    @Column(name = "class", nullable = false, length = 12)
    protected TransportClass transportClass;
    @Transient
    private static final double HOUR;

    static {
        HOUR = 60.0;
    }

    public Transport(TransportClass transportClass) {
        this.transportClass = transportClass;
    }

    /**
     * Parses duration(time) in a specific format - two characters after the floating point
     *
     * @param duration the duration of an excursion
     * @return the duration(time) in appropriate format
     */
    public LocalTime parseToLocalTime(Double duration) {
        String durationRound = String.format(Locale.US, "%.2f", duration);

        return generateTimestamp(durationRound);
    }

    /**
     * Parses the duration from string to LocalTime format
     *
     * @param durationString the time as a String
     * @return the time in LocalTime format
     */
    private LocalTime generateTimestamp(String durationString) {
        String minutesString = durationString.substring(durationString.indexOf('.') + 1, durationString.length());
        String hoursString = durationString.substring(0, durationString.indexOf('.'));
        int hours = Integer.parseInt(hoursString);
        int minutes = Integer.parseInt(minutesString);

        if (hours >= 24) {
            throw new FailedInitializationException("Improper transport");
        }

        while (minutes >= HOUR) {
            hours += 1;
            minutes -= HOUR;
        }

        return LocalTime.of(hours, minutes);
    }

    /**
     * Calculates the duration between two traveling points
     * based on the type of transport
     *
     * @param departurePoint   departure traveling point
     * @param destinationPoint destination traveling point
     * @return the duration
     */
    public abstract LocalTime calculateDuration(TravelingPoint departurePoint, TravelingPoint destinationPoint);
}