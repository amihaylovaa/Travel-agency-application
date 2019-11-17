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
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "source_id", referencedColumnName = "id")
    private TravelPoint source;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "destination_id", referencedColumnName = "id")
    private TravelPoint destination;
    @Column(name = "from", nullable = false)
    private LocalDate from;
    @Column(name = "to", nullable = false)
    private LocalDate to;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id")
    private Vehicle vehicle;

    public BookingData(LocalDate from, LocalDate to, TravelPoint source, TravelPoint destination, Vehicle vehicle) {
        setBookingDates(from, to);
        initializeFields(vehicle, source, destination);
    }

    private void initializeFields(Vehicle vehicle, TravelPoint source, TravelPoint destination) {
        if (vehicle == null) {
            throw new FailedInitializationException("Invalid vehicle");
        }
        if (source == null || destination == null) {
            throw new FailedInitializationException("Starting or ending point is not set");
        }
        this.vehicle = vehicle;
        this.source = source;
        this.destination = destination;
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