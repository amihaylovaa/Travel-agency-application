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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static adelina.luxtravel.utility.Utility.*;
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
        Booking newBooking = createBookingForUserThatDoesNotExist();
        User user = booking.getUser();
        long existingUserId = user.getId();

        lenient().when(userRepository.findById(existingUserId)).thenReturn(Optional.of(user));

        assertThrows(NonExistentItemException.class, () -> bookingService.save(newBooking));
    }

   /* @Test
    public void save_NotEnoughTickets_ExceptionThrown() {
        Booking booking = createBooking();
        TravelingData travelingData = booking.getTravelingData();
        Transport transport = travelingData.getTransport();
        DepartureDestination departureDestination = travelingData.getDepartureDestination();
        Date dates = travelingData.getDate();
        User user = booking.getUser();
        String username = user.getUsername();
        long travelingDataId = travelingData.getId();
        long bookingId = booking.getId();
        int availableTicketsCount = 5;
        int reservedTicketsCount = 7;
        TravelingData newTravelingData = new TravelingData(travelingDataId, departureDestination, transport, dates, availableTicketsCount);
        Booking newBooking = new Booking(bookingId, newTravelingData, user, reservedTicketsCount);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(travelingDataRepository.findById(travelingDataId)).thenReturn(Optional.of(travelingData));

        assertThrows(NonExistentItemException.class, () -> bookingService.save(newBooking));
    }
*/
    @Test
    public void save_ValidData_CreatedBooking() {
        Booking expectedBooking = createBooking();
        TravelingData travelingData = expectedBooking.getTravelingData();
        User user = expectedBooking.getUser();
        String username = user.getUsername();
        long travelingDataId = travelingData.getId();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(travelingDataRepository.findById(travelingDataId)).thenReturn(Optional.of(travelingData));
        when(bookingRepository.save(expectedBooking)).thenReturn(expectedBooking);

        Booking actualBooking = bookingService.save(expectedBooking);

        assertEquals(expectedBooking, actualBooking);
    }

    @Test
    public void findById_IdIsNegative_ExceptionThrown() {

        assertThrows(InvalidArgumentException.class, () -> bookingService.findById(NEGATIVE_ID));
    }

    @Test
    public void findById_BookingWithGivenIdDoesNotExist_ExceptionThrown() {
        Booking booking = createBooking();
        long existingBookingId = booking.getId();

        lenient().when(bookingRepository.findById(existingBookingId)).thenReturn(Optional.of(booking));

        assertThrows(NonExistentItemException.class, () -> bookingService.findById(NON_EXISTENT_ID));
    }

    @Test
    public void findById_ValidData_FoundBooking() {
        Booking expectedBooking = createBooking();
        long id = expectedBooking.getId();

        when(bookingRepository.findById(id)).thenReturn(Optional.of(expectedBooking));

        Booking actualBooking = bookingService.findById(id);

        assertEquals(expectedBooking, actualBooking);
    }

    @Test
    public void findAllUserBookings_UsernameIsEmpty_ExceptionThrown() {
        String username = "";

        assertThrows(InvalidArgumentException.class, () -> bookingService.findAllUserBookings(username));
    }

    @Test
    public void findAllUserBookings_UserWithGivenUsernameDoesNotExist_ExceptionThrown() {
        Booking booking = createBooking();
        User user = booking.getUser();
        String existingUsername = user.getUsername();
        String nonExistentUsername = "Joy83";

        lenient().when(userRepository.findByUsername(existingUsername)).thenReturn(Optional.of(user));

        assertThrows(NonExistentItemException.class, () -> bookingService.findAllUserBookings(nonExistentUsername));
    }

    @Test
    public void findAll_ListIsEmpty_ExceptionThrown() {
        List<Booking> bookings = new ArrayList<>();

        when(bookingRepository.findAll()).thenReturn(bookings);

        assertThrows(NonExistentItemException.class, () -> bookingService.findAll());
    }

    @Test
    void updateTickets_TicketsCountIsZero_ExceptionThrown() {
        Booking booking = createBooking();
        long bookingId = booking.getId();
        int ticketsCount = NumberUtils.INTEGER_ZERO;

        assertThrows(InvalidArgumentException.class, () -> bookingService.updateTickets(bookingId, ticketsCount));
    }

    @Test
    void updateTickets_BookingWithGivenIdDoesNotExist_ExceptionThrown() {
        Booking booking = createBooking();
        long existingBookingId = booking.getId();
        int ticketsCount = 5;

        lenient().when(bookingRepository.findById(existingBookingId)).thenReturn(Optional.of(booking));

        assertThrows(NonExistentItemException.class, () -> bookingService.updateTickets(NON_EXISTENT_ID, ticketsCount));
    }

    @Test
    void updateTickets_RequestedTicketsAreMoreThanTheAvailable_ExceptionThrown() {
        Booking booking = createBooking();
        long bookingId = booking.getId();
        int ticketsCount = 50;

        lenient().when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        assertThrows(NonExistentItemException.class, () -> bookingService.updateTickets(bookingId, ticketsCount));
    }

    @Test
    public void deleteById_IdIsNegative_ExceptionThrown() {

        assertThrows(InvalidArgumentException.class, () -> bookingService.deleteById(NEGATIVE_ID));
    }

    @Test
    public void deleteById_BookingWithGivenIdDoesNotExist_ExceptionThrown() {
        Booking booking = createBooking();
        long existingId = booking.getId();

        lenient().when(bookingRepository.findById(existingId)).thenReturn(Optional.of(booking));

        assertThrows(NonExistentItemException.class, () -> bookingService.deleteById(NON_EXISTENT_ID));
    }
}