package adelina.luxtravel.service;

import adelina.luxtravel.domain.*;
import adelina.luxtravel.exception.*;
import adelina.luxtravel.repository.BookingDataRepository;
import adelina.luxtravel.repository.BookingRepository;
import adelina.luxtravel.repository.UserRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {
    private BookingRepository bookingRepository;
    private BookingDataRepository bookingDataRepository;
    private UserRepository userRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, BookingDataRepository bookingDataRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.bookingDataRepository = bookingDataRepository;
        this.userRepository = userRepository;
    }

    public void save(Booking booking) {
        validateBooking(booking);
        bookingRepository.save(booking);

        BookingData bookingData = booking.getBookingData();

        bookingDataRepository.reserveTickets(booking.getTicketsCount(), bookingData.getId());
    }

    public Booking findById(long id) {
        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException("Invalid id");
        }
        return getExistingBooking(id);
    }

    public List<Booking> findAllBookingsByUsername(String username) {
        if (StringUtils.isEmpty(username)) {
            throw new InvalidArgumentException("Invalid username");
        }

        List<Booking> bookings = bookingRepository.findAllBookingsByUsername(username);

        if (ObjectUtils.isEmpty(bookings)) {
            throw new NonExistentItemException("Bookings for that user are not found");
        }
        return bookings;
    }

    public List<Booking> findAll() {
        List<Booking> bookings = bookingRepository.findAll();

        if (ObjectUtils.isEmpty(bookings)) {
            throw new NonExistentItemException("Bookings are not found");
        }
        return bookings;
    }

    public Booking updateTickets(long id, int ticketsCount) {
        Booking booking = findById(id);
        BookingData bookingData = booking.getBookingData();
        int availableTickets = bookingData.getCountAvailableTickets();

        if (ticketsCount > availableTickets) {
            throw new NonExistentItemException("Unavailable count of tickets, update can not be executed");
        }
        bookingRepository.updateByTickets(ticketsCount, id);
        return findById(id);
    }

    public void deleteBooking(long id) {
        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException("Invalid id");
        }

        Booking booking = findById(id);
        BookingData bookingData = booking.getBookingData();

        bookingDataRepository.cancelTicketReservation(booking.getTicketsCount(), bookingData.getId());
        bookingRepository.deleteById(id);
    }

    public void deleteAll() {
        bookingRepository.deleteAll();
    }

    private void validateBooking(Booking booking) {
        if (booking == null) {
            throw new InvalidArgumentException("Invalid booking");
        }
        validateBookingFields(booking);
    }

    private void validateBookingFields(Booking booking) {
        User user = booking.getUser();
        BookingData bookingData = booking.getBookingData();
        int ticketsCount = booking.getTicketsCount();

        if (user == null) {
            throw new InvalidArgumentException("Invalid user");
        }
        if (bookingData == null) {
            throw new InvalidArgumentException("Invalid booking data");
        }
        if (ticketsCount <= NumberUtils.INTEGER_ZERO) {
            throw new InvalidArgumentException("Tickets' count can not be less than or equal to zero");
        }
        validateFieldsExist(user, bookingData, ticketsCount);
    }

    private void validateFieldsExist(User user, BookingData bookingData, int countTickets) {
        long userId = user.getId();
        long bookingDataId = bookingData.getId();

        if (!userRepository.findById(userId).isPresent()) {
            throw new NonExistentItemException("User does not exist");
        }
        if (bookingDataRepository.findById(bookingDataId) == null) {
            throw new NonExistentItemException("Booking data does not exist");
        }
        if (countTickets > bookingData.getCountAvailableTickets()) {
            throw new NonExistentItemException("Unavailable tickets");
        }
    }

    private Booking getExistingBooking(long id) {
        Booking booking = bookingRepository.findById(id);
        if (booking == null) {
            throw new NonExistentItemException("This booking does not exist");
        }
        return booking;
    }
}