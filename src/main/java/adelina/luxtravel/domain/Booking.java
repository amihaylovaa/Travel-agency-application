package adelina.luxtravel.domain;

import adelina.luxtravel.exception.FailedInitializationException;
import adelina.luxtravel.wrapper.BookingData;
import lombok.Getter;

import javax.persistence.Entity;

@Getter
public class Booking {
    private double price;
    private User user;
    private BookingData bookingData;

    public Booking(double price, BookingData bookingData) {
        initializeFields(price, bookingData);
    }

    private void initializeFields(double price, BookingData bookingData) {
        if (bookingData == null){
            throw new FailedInitializationException("Null booking date");
        }
        this.bookingData = bookingData;
        calculatePrice();
    }

    private void calculatePrice() {
        //  double durationInMinutes = getDurationInMinutes();

        //   if (vehicle instanceof Airplane) {
        //    AirplaneClass airplaneClass = ((Airplane) vehicle).getAirplaneClass();
        //   price = durationInMinutes / airplaneClass.getPriceCoefficient();
        // } else {
        // price = durationInMinutes / TEN_PERCENT;
        //}
    }

    //  private double getDurationInMinutes() {
    //  LocalTime duration = vehicle.calculateDuration(endingPoint);

    //   return (duration.getHour() * MINUTE) + duration.getMinute();
    // }
}