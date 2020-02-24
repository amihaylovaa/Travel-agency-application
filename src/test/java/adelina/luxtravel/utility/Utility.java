package adelina.luxtravel.utility;

import adelina.luxtravel.domain.Booking;
import adelina.luxtravel.domain.TravelingData;
import adelina.luxtravel.domain.TravelingPoint;
import adelina.luxtravel.domain.User;
import adelina.luxtravel.domain.transport.Airplane;
import adelina.luxtravel.domain.transport.Bus;
import adelina.luxtravel.domain.transport.Transport;
import adelina.luxtravel.domain.transport.TransportClass;
import adelina.luxtravel.domain.wrapper.Date;
import adelina.luxtravel.domain.wrapper.DepartureDestination;
import org.apache.commons.lang3.math.NumberUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class Utility {

    public static final long NON_EXISTENT_ID = 43_932_21;
    public static final long NEGATIVE_ID = NumberUtils.LONG_MINUS_ONE;
    public static final long ZERO_ID = NumberUtils.LONG_ZERO;

    public static final User createUser() {
        String username = "violet_sun12";
        String email = "violet_sun12@gmail.com";
        String password = "12345678910";

        return new User(username, email, password);
    }

    public static final TravelingPoint createTravelingPoint() {
        String name = "Quebec,Canada";
        double longitude = -71.25;
        double latitude = 46.82;
        long id = NumberUtils.LONG_ONE;

        return new TravelingPoint(id, name, longitude, latitude);
    }

    public static final List<TravelingPoint> createTravelingPointList() {
        String name = "Germany";
        double latitude = 51.13;
        double longitude = 10.01;
        long id = 2;
        TravelingPoint firstTravelingPoint = createTravelingPoint();
        TravelingPoint secondTravelingPoint = new TravelingPoint(id, name, longitude, latitude);
        List<TravelingPoint> travelingPoints = new ArrayList<>();

        travelingPoints.add(firstTravelingPoint);
        travelingPoints.add(secondTravelingPoint);

        return travelingPoints;
    }

    public static final List<Transport> createTransportList() {
        TransportClass transportClassFirst = TransportClass.FIRST;
        TransportClass transportClassBusiness = TransportClass.BUSINESS;
        TransportClass transportClassEconomy = TransportClass.ECONOMY;
        Transport transportAirplaneFirstClass = new Airplane(1, transportClassFirst);
        Transport transportAirplaneBusiness = new Airplane(2, transportClassBusiness);
        Transport transportBusBusiness = new Bus(3, transportClassBusiness);
        Transport transportBusEconomy = new Bus(4, transportClassEconomy);
        Transport transportSecondBusEconomy = new Bus(5, transportClassEconomy);
        List<Transport> transports = new ArrayList<>();

        transports.add(transportAirplaneBusiness);
        transports.add(transportAirplaneFirstClass);
        transports.add(transportBusBusiness);
        transports.add(transportBusEconomy);
        transports.add(transportSecondBusEconomy);

        return transports;
    }

    public static final TravelingData createTravelingData() {
        long id = NumberUtils.LONG_ONE;
        int availableTicketsCount = 36;
        double departureLatitude = 42.13;
        double departureLongitude = 24.74;
        double destinationLatitude = 8.73;
        double destinationLongitude = 76.71;
        String departureName = "Plovdiv, Bulgaria";
        String destinationName = "Varkala, Kerala, India";
        TravelingPoint departurePoint = new TravelingPoint(departureName, departureLongitude, departureLatitude);
        TravelingPoint destinationPoint = new TravelingPoint(destinationName, destinationLongitude, destinationLatitude);
        DepartureDestination departureDestination = new DepartureDestination(departurePoint, destinationPoint);
        TransportClass transportClass = TransportClass.FIRST;
        Transport transport = new Airplane(id, transportClass);
        LocalDate from = LocalDate.of(2020, 4, 21);
        LocalDate to = LocalDate.of(2020, 4, 30);
        Date date = new Date(from, to);

        return new TravelingData(id, departureDestination, transport, date, availableTicketsCount);
    }

    public static final List<TravelingData> createTravelingDataList() {
        long id = 2;
        int availableTicketsCount = 10;
        double departureLatitude = 42.69;
        double departureLongitude = 23.31;
        double destinationLatitude = 47.31;
        double destinationLongitude = 5.01;
        String departureName = "Sofia, Bulgaria";
        String destinationName = "Dijon, the Bourgogne-Franche-Comté, France";
        TravelingPoint departurePoint = new TravelingPoint(departureName, departureLongitude, departureLatitude);
        TravelingPoint destinationPoint = new TravelingPoint(destinationName, destinationLongitude, destinationLatitude);
        DepartureDestination departureDestination = new DepartureDestination(departurePoint, destinationPoint);
        TransportClass transportClass = TransportClass.BUSINESS;
        Transport transport = new Airplane(id, transportClass);
        LocalDate from = LocalDate.of(2020, 10, 10);
        LocalDate to = LocalDate.of(2020, 10, 12);
        Date date = new Date(from, to);
        TravelingData travelingDataA = new TravelingData(id, departureDestination, transport, date, availableTicketsCount);
        TravelingData travelingDataB = createTravelingData();
        List<TravelingData> travelingData = new ArrayList<>();

        travelingData.add(travelingDataA);
        travelingData.add(travelingDataB);

        return travelingData;
    }

    public static final TravelingData createTravelingDataWithNonExistingTransport() {
        TravelingData travelingData = createTravelingData();
        long id = 2;
        int availableTicketsCount = travelingData.getAvailableTicketsCount();
        Transport nonExistingTransport = new Airplane(id, TransportClass.FIRST);
        DepartureDestination departureDestination = travelingData.getDepartureDestination();
        Date dates = travelingData.getDate();

        return new TravelingData(id, departureDestination, nonExistingTransport, dates, availableTicketsCount);
    }

    public static final Booking createBooking() {
        long id = 1;
        int reservedTicketsCount = 4;
        User user = createUser();
        TravelingData travelingData = createTravelingData();

        return new Booking(id, travelingData, user, reservedTicketsCount);
    }

    public static final Booking createBookingForUserThatDoesNotExist() {
        Booking booking = createBooking();
        TravelingData travelingData = booking.getTravelingData();
        User user = booking.getUser();
        String username = user.getUsername();
        String email = user.getEmail();
        String password = user.getPassword();
        long bookingId = booking.getId();
        int reservedTicketsCount = booking.getReservedTicketsCount();
        User nonExistingUser = new User(NON_EXISTENT_ID, username, email, password);

        return new Booking(bookingId, travelingData, nonExistingUser, reservedTicketsCount);
    }
}