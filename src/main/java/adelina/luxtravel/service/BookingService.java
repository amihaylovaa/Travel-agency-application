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

        bookingDataRepository.decrementCountAvailableTickets(booking.getCountTickets(), bookingData.getId());
    }

    public Booking findBookingById(long id) {
        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException("Invalid id");
        }
        return getExistingBooking(bookingRepository.findById(id));
    }

    public List<Booking> findAllBookingsByUserName(String username) {
        if (StringUtils.isEmpty(username)) {
            throw new InvalidArgumentException("Invalid username");
        }
        return getExistingBooking(bookingRepository.findAllBookingsByUsername(username));
    }

    public List<Booking> findAll() {
        return getExistingBooking(bookingRepository.findAll());
    }



    public void deleteBooking(long id) {
        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException("Invalid id");
        }

        Booking booking = findBookingById(id);
        BookingData bookingData = booking.getBookingData();

        bookingDataRepository.incrementCountAvailableTickets(booking.getCountTickets(), bookingData.getId());
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
        int countTickets = booking.getCountTickets();

        if (user == null || bookingData == null || countTickets <= NumberUtils.INTEGER_ZERO) {
            throw new InvalidArgumentException("Invalid booking fields");
        }
        validateFieldsExist(user, bookingData, countTickets);
    }

    private void validateFieldsExist(User user, BookingData bookingData, int countTickets) {
        long userId = user.getId();
        long bookingDataId = bookingData.getId();

        if (userRepository.findById(userId) == null || bookingDataRepository.findById(bookingDataId) == null) {
            throw new NonExistentItemException("User or booking data does not exist");
        }
        if (countTickets > bookingData.getCountAvailableTickets()) {
            throw new NonExistentItemException("Unavailable tickets");
        }
    }

    private List<Booking> getExistingBooking(List<Booking> bookings) {
        if (ObjectUtils.isEmpty(bookings)) {
            throw new NonExistentItemException("Bookings did not exist");
        }
        return bookings;
    }

    private Booking getExistingBooking(Booking booking) {
        if (booking == null) {
            throw new NonExistentItemException("Booking does not exist");
        }
        return booking;
    }
}