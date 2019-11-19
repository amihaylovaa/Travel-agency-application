package adelina.luxtravel.domain.wrapper;

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
        setVehicle(vehicle);
        setSourceDestinationId(sourceDestinationId);
    }

    private void setSourceDestinationId(SourceDestinationId sourceDestinationId) {
        if (sourceDestinationId == null) {
            throw new FailedInitializationException("Starting or ending point is not set");
        }
        this.sourceDestinationId = sourceDestinationId;
    }

    private void setVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            throw new FailedInitializationException("Invalid vehicle");
        }
        this.vehicle = vehicle;
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