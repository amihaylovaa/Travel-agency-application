package adelina.luxtravel.service;

import adelina.luxtravel.domain.*;
import adelina.luxtravel.exception.*;
import adelina.luxtravel.repository.TravelingDataRepository;
import adelina.luxtravel.repository.BookingRepository;
import adelina.luxtravel.repository.UserRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static adelina.luxtravel.utility.Constants.INVALID_ID;

@Service
public class BookingService {
    private BookingRepository bookingRepository;
    private TravelingDataRepository travelingDataRepository;
    private UserRepository userRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, TravelingDataRepository travelingDataRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.travelingDataRepository = travelingDataRepository;
        this.userRepository = userRepository;
    }

    public Booking save(Booking booking) throws InvalidArgumentException, NonExistentItemException {
        if (booking == null) {
            throw new InvalidArgumentException("Invalid booking");
        }
        validateFieldsExist(booking);
        reserveTickets(booking);
        return bookingRepository.save(booking);
    }

    public Booking findById(long id) throws InvalidArgumentException, NonExistentItemException {
        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException(INVALID_ID);
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

    public void updateTickets(long id, int reservedTicketsCount) throws InvalidArgumentException, NonExistentItemException {
        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException("Invalid id");
        }
        if (reservedTicketsCount <= NumberUtils.INTEGER_ZERO) {
            throw new InvalidArgumentException("Invalid tickets count");
        }

        validateTicketsUpdate(id, reservedTicketsCount);

        bookingRepository.updateByTickets(reservedTicketsCount, id);
        travelingDataRepository.reserveTickets(reservedTicketsCount, id);
    }

    public void deleteById(long id) throws InvalidArgumentException, NonExistentItemException {
        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException(INVALID_ID);
        }
        cancelTicketsReservation(id);
        bookingRepository.deleteById(id);
    }

    private void validateFieldsExist(Booking booking) throws NonExistentItemException {
        User user = booking.getUser();
        TravelingData travelingData = booking.getTravelingData();
        int reservedTicketsCount = booking.getReservedTicketsCount();
        long userId = user.getId();
        long bookingDataId = travelingData.getId();
        int availableTicketsCount = travelingData.getAvailableTicketsCount();

        Optional<User> searchedUser = userRepository.findById(userId);

        if (!searchedUser.isPresent()) {
            throw new NonExistentItemException("User does not exist");
        }

        Optional<TravelingData> searchedBookingData = travelingDataRepository.findById(bookingDataId);

        if (!searchedBookingData.isPresent()) {
            throw new NonExistentItemException("Booking data does not exist");
        }

        validateTicketsAreSufficient(reservedTicketsCount, availableTicketsCount);
    }

    private void validateTicketsAreSufficient(int reservedTicketsCount, int availableTicketsCount) throws NonExistentItemException {
        if (reservedTicketsCount > availableTicketsCount) {
            throw new NonExistentItemException("Unavailable tickets count");
        }
    }

    private void reserveTickets(Booking booking) {
        long bookingDataId = getBookingDataId(booking);
        int ticketsCount = booking.getReservedTicketsCount();

        travelingDataRepository.reserveTickets(ticketsCount, bookingDataId);
    }

    private void cancelTicketsReservation(long bookingId) throws InvalidArgumentException, NonExistentItemException {
        Booking booking = findById(bookingId);
        int reservedTicketsCount = booking.getReservedTicketsCount();
        long bookingDataId = getBookingDataId(booking);

        travelingDataRepository.cancelTicketReservation(reservedTicketsCount, bookingDataId);
    }

    private long getBookingDataId(Booking booking) {
        TravelingData travelingData = booking.getTravelingData();

        return travelingData.getId();
    }

    private void validateTicketsUpdate(long bookingId, int newTicketsCount)
            throws InvalidArgumentException, NonExistentItemException {
        Booking booking = findById(bookingId);
        TravelingData travelingData = booking.getTravelingData();
        int availableTicketsCount = travelingData.getAvailableTicketsCount();
        int reservedTicketsCount = booking.getReservedTicketsCount();

        validateTicketsAreSufficient(newTicketsCount, availableTicketsCount);
        travelingDataRepository.updateTicketsReservation(reservedTicketsCount, bookingId);
    }
}