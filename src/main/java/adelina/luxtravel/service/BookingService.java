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
import java.util.Optional;

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

    public Booking save(Booking booking) throws InvalidArgumentException, NonExistentItemException {
        validateBooking(booking);
        reserveTickets(booking);
        return bookingRepository.save(booking);
    }

    public Booking findById(long id) throws InvalidArgumentException, NonExistentItemException {
        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException("Invalid id");
        }

        Optional<Booking> booking = bookingRepository.findById(id);

        if (!booking.isPresent()) {
            throw new NonExistentItemException("This booking does not exist");
        }
        return booking.get();
    }

    public List<Booking> findAllUserBookings(String username)
            throws InvalidArgumentException, NonExistentItemException {
        if (StringUtils.isEmpty(username)) {
            throw new InvalidArgumentException("Invalid username");
        }

        List<Booking> bookings = bookingRepository.findAllUserBookings(username);

        if (ObjectUtils.isEmpty(bookings)) {
            throw new NonExistentItemException("Bookings for this user are not found");
        }
        return bookings;
    }

    public List<Booking> findAll() throws NonExistentItemException {
        List<Booking> bookings = bookingRepository.findAll();

        if (ObjectUtils.isEmpty(bookings)) {
            throw new NonExistentItemException("Bookings are not found");
        }
        return bookings;
    }

    public void updateTickets(long id, int newTicketsCount) throws InvalidArgumentException, NonExistentItemException {
        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException("Invalid id");
        }
        if (newTicketsCount <= NumberUtils.INTEGER_ZERO) {
            throw new InvalidArgumentException("Invalid tickets count");
        }

        validateUpdateIsPossible(id, newTicketsCount);

        bookingRepository.updateByTickets(newTicketsCount, id);
        bookingDataRepository.reserveTickets(newTicketsCount, id);
    }

    public void deleteById(long id) throws InvalidArgumentException, NonExistentItemException {
        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException("Invalid id");
        }
        cancelTicketsReservation(id);
        bookingRepository.deleteById(id);
    }

    private void validateBooking(Booking booking) throws InvalidArgumentException, NonExistentItemException {
        if (booking == null) {
            throw new InvalidArgumentException("Invalid booking");
        }
        validateBookingFields(booking);
    }

    private void validateBookingFields(Booking booking)
            throws InvalidArgumentException, NonExistentItemException {
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

    private void validateFieldsExist(User user, BookingData bookingData, int ticketsCount) throws NonExistentItemException {
        long userId = user.getId();
        long bookingDataId = bookingData.getId();
        int availableTicketsCount = bookingData.getAvailableTicketsCount();

        Optional<User> searchedUser = userRepository.findById(userId);

        if (!searchedUser.isPresent()) {
            throw new NonExistentItemException("User does not exist");
        }

        Optional<BookingData> searchedBookingData = bookingDataRepository.findById(bookingDataId);

        if (!searchedBookingData.isPresent()) {
            throw new NonExistentItemException("Booking data does not exist");
        }

        if (ticketsCount > availableTicketsCount) {
            throw new NonExistentItemException("Unavailable tickets");
        }
    }

    private void reserveTickets(Booking booking) {
        long bookingDataId = getBookingDataId(booking);
        int ticketsCount = booking.getTicketsCount();

        bookingDataRepository.reserveTickets(ticketsCount, bookingDataId);
    }

    private void cancelTicketsReservation(long bookingId) throws InvalidArgumentException, NonExistentItemException {
        Booking booking = findById(bookingId);
        int ticketsCount = booking.getTicketsCount();
        long bookingDataId = getBookingDataId(booking);

        bookingDataRepository.cancelTicketReservation(ticketsCount, bookingDataId);
    }

    private long getBookingDataId(Booking booking) {
        BookingData bookingData = booking.getBookingData();

        return bookingData.getId();
    }

    private void validateUpdateIsPossible(long id, int newTicketsCount)
            throws InvalidArgumentException, NonExistentItemException {
        Booking booking = findById(id);
        BookingData bookingData = booking.getBookingData();
        int availableTicketsCount = bookingData.getAvailableTicketsCount();
        int currentTicketsCount = booking.getTicketsCount();

        if (newTicketsCount > availableTicketsCount) {
            throw new NonExistentItemException("Unavailable count of tickets, update can not be executed");
        }
        bookingDataRepository.cancelTicketReservation(currentTicketsCount, id);
    }
}