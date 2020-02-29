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
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

import static adelina.luxtravel.utility.Constants.INVALID_ID;
import static adelina.luxtravel.utility.Constants.INVALID_USERNAME;


@Service
@Transactional
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

    public Booking save(Booking booking) {
        if (booking == null) {
            throw new InvalidArgumentException("Invalid booking");
        }

        validateFields(booking);
        reserveTickets(booking);

        return bookingRepository.save(booking);
    }

    public Booking findById(long id) {
        validateId(id);

        Optional<Booking> booking = bookingRepository.findById(id);

        if (!booking.isPresent()) {
            throw new NonExistentItemException("This booking does not exist");
        }
        return booking.get();
    }

    public List<Booking> findAllUserBookings(String username) {
        if (StringUtils.isEmpty(username)) {
            throw new InvalidArgumentException(INVALID_USERNAME);
        }

        validateUserExists(username);

        List<Booking> bookings = bookingRepository.findAllUserBookings(username);

        if (ObjectUtils.isEmpty(bookings)) {
            throw new NonExistentItemException("Bookings for this user are not found");
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

    public void updateTickets(long bookingId, int reservedTicketsCount) {
        validateId(bookingId);

        if (reservedTicketsCount <= NumberUtils.INTEGER_ZERO) {
            throw new InvalidArgumentException("Invalid tickets count");
        }

        validateTicketsUpdateParameters(bookingId, reservedTicketsCount);

        bookingRepository.updateByTickets(reservedTicketsCount, bookingId);
    }

    public void deleteById(long id) {
        validateId(id);

        Optional<Booking> booking = bookingRepository.findById(id);

        if (!booking.isPresent()) {
            throw new NonExistentItemException("This booking does not exist");
        }
        cancelTicketsReservation(id);
        bookingRepository.deleteById(id);
    }

    private void validateFields(Booking booking) {
        User user = booking.getUser();
        TravelingData travelingData = booking.getTravelingData();
        int reservedTicketsCount = booking.getReservedTicketsCount();

        if (user == null) {
            throw new InvalidArgumentException("Invalid user");
        }

        if (travelingData == null) {
            throw new InvalidArgumentException("Invalid traveling data");
        }

        long id = travelingData.getId();
        Optional<TravelingData> searchedTravelingData = travelingDataRepository.findById(id);

        if (!searchedTravelingData.isPresent()) {
            throw new NonExistentItemException("Traveling data does not exist");
        }

        validateUserExists(user.getUsername());
        validateTicketsAreSufficient(reservedTicketsCount, searchedTravelingData.get().getAvailableTicketsCount());
    }

    private void validateUserExists(String username) {
        Optional<User> searchedUser = userRepository.findByUsername(username);

        if (!searchedUser.isPresent()) {
            throw new NonExistentItemException("User does not exist");
        }
    }

    private void validateTicketsAreSufficient(int reservedTicketsCount, int availableTicketsCount) {
        if (reservedTicketsCount > availableTicketsCount) {
            throw new NonExistentItemException("Unavailable tickets count");
        }
    }

    private void reserveTickets(Booking booking) {
        long travelingDataId = getTravelingDataId(booking);
        int ticketsCount = booking.getReservedTicketsCount();

        travelingDataRepository.reserveTickets(ticketsCount, travelingDataId);
    }

    private void cancelTicketsReservation(long bookingId) {
        Booking booking = findById(bookingId);
        int reservedTicketsCount = booking.getReservedTicketsCount();
        long travelingDataId = getTravelingDataId(booking);

        travelingDataRepository.cancelTicketReservation(reservedTicketsCount, travelingDataId);
    }

    private long getTravelingDataId(Booking booking) {
        TravelingData travelingData = booking.getTravelingData();

        return travelingData.getId();
    }

    private void validateId(long id) {
        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException(INVALID_ID);
        }
    }

    private void validateTicketsUpdateParameters(long bookingId, int newTicketsCount) {
        Booking booking = findById(bookingId);
        TravelingData travelingData = booking.getTravelingData();
        long travelingDataId = travelingData.getId();
        int currentReservedTicketsCount = booking.getReservedTicketsCount();

        travelingDataRepository.cancelTicketReservation(currentReservedTicketsCount, travelingDataId);

        int availableTicketsCount = travelingDataRepository.findAvailableTicketsCount(travelingDataId);

        validateTicketsAreSufficient(newTicketsCount, availableTicketsCount);

        travelingDataRepository.reserveTickets(newTicketsCount, travelingDataId);
    }
}