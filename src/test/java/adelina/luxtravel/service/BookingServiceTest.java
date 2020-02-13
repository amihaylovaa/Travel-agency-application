package adelina.luxtravel.service;

import adelina.luxtravel.domain.Booking;
import adelina.luxtravel.domain.TravelingData;
import adelina.luxtravel.domain.TravelingPoint;
import adelina.luxtravel.domain.User;
import adelina.luxtravel.domain.transport.Airplane;
import adelina.luxtravel.domain.transport.Transport;
import adelina.luxtravel.domain.transport.TransportClass;
import adelina.luxtravel.domain.wrapper.Date;
import adelina.luxtravel.domain.wrapper.DepartureDestination;
import adelina.luxtravel.exception.InvalidArgumentException;
import adelina.luxtravel.exception.NonExistentItemException;
import adelina.luxtravel.repository.BookingRepository;
import adelina.luxtravel.repository.TravelingDataRepository;
import adelina.luxtravel.repository.UserRepository;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    @Mock
    TravelingDataRepository travelingDataRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    BookingRepository bookingRepository;
    @InjectMocks
    BookingService bookingService;

    @Test
    public void save_BookingIsNull_ExceptionThrown() {
        Booking booking = null;

        assertThrows(InvalidArgumentException.class, () -> bookingService.save(booking));
    }

    @Test
    public void save_UserWithGivenIdDoesNotExist_ExceptionThrown() {
        Booking booking = createBooking();
        TravelingData travelingData = booking.getTravelingData();
        User user = booking.getUser();
        String username = user.getUsername();
        String email = user.getEmail();
        String password = user.getPassword();
        long existingUserId = user.getId();
        long nonExistingUserId = 2;
        long bookingId = booking.getId();
        int reservedTicketsCount = booking.getReservedTicketsCount();
        User nonExistingUser = new User(nonExistingUserId, username, email, password);
        Booking newBooking = new Booking(bookingId, travelingData, nonExistingUser, reservedTicketsCount);

        lenient().when(userRepository.findById(existingUserId)).thenReturn(Optional.of(user));

        assertThrows(NonExistentItemException.class, () -> bookingService.save(newBooking));
    }

    @Test
    void findById() {
    }

    @Test
    void findAllUserBookings() {
    }

    @Test
    void findAll() {
    }

    @Test
    void updateTickets() {
    }

    @Test
    public void deleteById_IdIsNegative_ExceptionThrown() {
        long id = NumberUtils.LONG_MINUS_ONE;

        assertThrows(InvalidArgumentException.class, () -> bookingService.deleteById(id));
    }

    @Test
    public void deleteById_BookingWithGivenIdDoesNotExist_ExceptionThrown() {
        Booking booking = createBooking();
        long existingId = booking.getId();
        long nonExistingId = 12;

        lenient().when(bookingRepository.findById(existingId)).thenReturn(Optional.of(booking));

        assertThrows(NonExistentItemException.class, () -> bookingService.deleteById(nonExistingId));
    }

    private Booking createBooking() {
        long id = 1;
        int reservedTicketsCount = 4;
        User user = createUser();
        TravelingData travelingData = createTravelingData();

        return new Booking(id, travelingData, user, reservedTicketsCount);
    }

    private TravelingData createTravelingData() {
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

    private User createUser() {
        long id = NumberUtils.LONG_ONE;
        String username = "violet_sun12";
        String email = "violet_sun12@gmail.com";
        String password = "12345678910";

        return new User(id, username, email, password);
    }
}