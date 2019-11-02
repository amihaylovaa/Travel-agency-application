package adelina.luxtravel.domain;

import adelina.luxtravel.domain.transport.Airplane;
import adelina.luxtravel.domain.transport.Vehicle;
import adelina.luxtravel.exception.FailedInitializationException;

import static adelina.luxtravel.utility.Constants.MINUTE;

import java.time.LocalDate;
import java.time.LocalTime;

public class Booking {
    private double price;
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
        if (startingPoint == null || endingPoint == null) {
            throw new FailedInitializationException("Starting or ending point is not set");
        }
        this.from = from;
        this.to = to;
        this.vehicle = vehicle;
        this.startingPoint = startingPoint;
        this.endingPoint = endingPoint;
        calculatePrice();
    }

    private void calculatePrice() {
        double durationInMinutes = getDurationInMinutes();

        if (vehicle instanceof Airplane) {
        //    AirplaneClass airplaneClass = ((Airplane) vehicle).getAirplaneClass();
         //   price = durationInMinutes / airplaneClass.getPriceCoefficient();
        } else {
           // price = durationInMinutes / TEN_PERCENT;
        }
    }

    private double getDurationInMinutes() {
        LocalTime duration = vehicle.calculateDuration(endingPoint);

        return (duration.getHour() * MINUTE) + duration.getMinute();
    }
}