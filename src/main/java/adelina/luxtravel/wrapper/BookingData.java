package adelina.luxtravel.wrapper;

import adelina.luxtravel.domain.transport.Vehicle;
import adelina.luxtravel.exception.FailedInitializationException;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "booking_data")
@Getter
public class BookingData {
    @EmbeddedId
    SourceDestinationId sourceDestinationId;
    @Column(name = "fromDate", nullable = false)
    private LocalDate from;
    @Column(name = "toDate", nullable = false)
    private LocalDate to;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @MapsId("travel_point")
    @ManyToOne
    TravelPoint source;
    @MapsId("travel_point")
    @ManyToOne
    TravelPoint destination;

    public BookingData(LocalDate from, LocalDate to, Vehicle vehicle, SourceDestinationId sourceDestinationId) {
        setBookingDates(from, to);
        initializeFields(vehicle, sourceDestinationId);
    }

    private void initializeFields(Vehicle vehicle, SourceDestinationId sourceDestinationId) {
        if (vehicle == null) {
            throw new FailedInitializationException("Invalid vehicle");
        }
        if (sourceDestinationId == null) {
            throw new FailedInitializationException("Starting or ending point is not set");
        }
        this.vehicle = vehicle;
        this.sourceDestinationId = sourceDestinationId;
    }

    private void setBookingDates(LocalDate from, LocalDate to) {
        try {
            if (from.isAfter(to) || from.isEqual(to) || from.isBefore(LocalDate.now())) {
                throw new FailedInitializationException("Invalid dates");
            }
        } catch (NullPointerException npe) {
            throw new FailedInitializationException("Null dates are forbidden");
        }
        this.from = from;
        this.to = to;
    }
}