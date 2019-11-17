package adelina.luxtravel.wrapper;

import adelina.luxtravel.domain.transport.Vehicle;
import adelina.luxtravel.exception.FailedInitializationException;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class BookingData {
   private LocalDate from;
   private LocalDate to;
   private TravelPoint source;
   private TravelPoint destination;
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