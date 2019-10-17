package adelina.luxtravel.domain;

import adelina.luxtravel.domain.transport.Vehicle;
import adelina.luxtravel.exception.FailedInitializationException;

import java.time.LocalDate;

public class Booking {
    private LocalDate from;
    private LocalDate to;
    private Vehicle vehicle;
    private City startingPoint;
    private City endingPoint;

    public Booking(LocalDate from, LocalDate to, Vehicle vehicle, City startingPoint, City endingPoint) {
        initializeFields(from, to, vehicle, startingPoint, endingPoint);
    }

    private void initializeFields(LocalDate from, LocalDate to, Vehicle vehicle, City startingPoint, City endingPoint) {
        if (from.isAfter(to) || from.isEqual(to) || from.isBefore(LocalDate.now())) {
            throw new FailedInitializationException("Invalid dates");
        }
        if (vehicle == null) {
            throw new FailedInitializationException("Invalid vehicle");
        }
        if (startingPoint == null  || endingPoint == null ) {
            throw new FailedInitializationException("Starting or ending point is not set");
        }
        this.from = from;
        this.to = to;
        this.vehicle = vehicle;
        this.startingPoint = startingPoint;
        this.endingPoint = endingPoint;
    }

    private double generatePrice() {
        double price = 0.00;
        vehicle.findDuration(startingPoint, endingPoint);

        return price;
    }
}