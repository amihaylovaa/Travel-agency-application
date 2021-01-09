package adelina.luxtravel.service;

import adelina.luxtravel.domain.*;
import adelina.luxtravel.domain.transport.Transport;
import adelina.luxtravel.domain.wrapper.ExcursionTransport;
import adelina.luxtravel.exception.*;
import adelina.luxtravel.repository.BookingRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

import static adelina.luxtravel.utility.Constants.*;


/**
 * Represents service for a booking
 */
@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ExcursionService excursionService;
    private final UserService userService;

    @Autowired
    public BookingService(BookingRepository bookingRepository, ExcursionService excursionService, UserService userService) {
        this.bookingRepository = bookingRepository;
        this.excursionService = excursionService;
        this.userService = userService;
    }

    /**
     * Saves new booking
     *
     * @param booking the booking that is going to be saved
     * @return the saved booking
     */
    public Booking save(Booking booking) {
        if (ObjectUtils.isEmpty(booking)) {
            throw new InvalidArgumentException("Invalid booking");
        }

        validateFields(booking);
        reserveTickets(booking);
        Booking storedBooking = bookingRepository.save(booking);
        long storedBookingId = storedBooking.getId();

        bookingRepository.addReservationDate();

        return findById(storedBookingId);
    }

    /**
     * Gets booking by id
     *
     * @param id booking's id
     * @return if present - the searched booking, otherwise throws exception for non-existent booking
     */
    public Booking findById(long id) {
        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException(INVALID_ID);
        }

        Optional<Booking> booking = bookingRepository.findById(id);

        if (!booking.isPresent()) {
            throw new NonExistentItemException("This booking does not exist");
        }
        return booking.get();
    }

    /**
     * Gets user's bookings
     *
     * @param username user's username
     * @return list of user's bookings, otherwise throws exception for non-existent bookings for a user with that username
     */
    public List<Booking> findAllUserBookings(String username) {
        if (StringUtils.isEmpty(username)) {
            throw new InvalidArgumentException(INVALID_USERNAME);
        }

        userService.findByUsername(username);

        List<Booking> bookings = bookingRepository.findAllUserBookings(username);

        if (ObjectUtils.isEmpty(bookings)) {
            throw new NonExistentItemException("Bookings for this user are not found");
        }
        return bookings;
    }

    /**
     * Gets all bookings
     *
     * @return list of all bookings, throws exception if bookings are not found
     */
    public List<Booking> findAll() {
        List<Booking> bookings = bookingRepository.findAll();

        if (ObjectUtils.isEmpty(bookings)) {
            throw new NonExistentItemException("Bookings are not found");
        }
        return bookings;
    }

    /**
     * Updates the count of reserved tickets by booking's id
     *
     * @param id                      booking's id
     * @param newReservedTicketsCount the new tickets' count
     */
    public Booking updateReservedTicketsCount(long id, int newReservedTicketsCount) {
        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException(INVALID_ID);
        }
        if (newReservedTicketsCount <= NumberUtils.INTEGER_ZERO) {
            throw new InvalidArgumentException(INVALID_TICKETS_COUNT);
        }

        Booking booking = findById(id);
        long excursionId = getExcursionId(booking);
        int currentReservedTicketsCount = booking.getReservedTicketsCount();
        int availableTicketsCount = excursionService.cancelTicketsReservation(excursionId, currentReservedTicketsCount);

        if (areTicketsSufficient(newReservedTicketsCount, availableTicketsCount)) {
            excursionService.reserveTickets(excursionId, newReservedTicketsCount);
            bookingRepository.updateReservedTicketsCount(newReservedTicketsCount, id);

            return findById(id);
        }
        throw new InvalidArgumentException("Unavailable tickets count for this excursion");
    }

    /**
     * Deletes booking by id
     *
     * @param id booking's id
     * @return true for successfully deleted booking, false for unsuccessful attempt
     */
    public boolean deleteById(long id) {
        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException(INVALID_ID);
        }

        Booking booking = findById(id);
        int reservedTicketsCount = booking.getReservedTicketsCount();
        long excursionId = getExcursionId(booking);

        excursionService.cancelTicketsReservation(excursionId, reservedTicketsCount);
        bookingRepository.deleteById(id);

        try {
            findById(id);
        } catch (NonExistentItemException e) {
            return true;
        }
        return false;
    }

    private void validateFields(Booking booking) {
        User user = booking.getUser();
        ExcursionTransport excursionTransport = booking.getExcursionTransport();
        Excursion excursion = excursionTransport.getExcursion();
        Transport transport = excursionTransport.getTransport();
        int reservedTicketsCount = booking.getReservedTicketsCount();

        if (ObjectUtils.isEmpty(user)) {
            throw new InvalidArgumentException(INVALID_USER);
        }

        if (ObjectUtils.isEmpty(excursionTransport)) {
            throw new InvalidArgumentException("Invalid excursion-transport");
        }

        if (ObjectUtils.isEmpty(excursion)) {
            throw new InvalidArgumentException("Invalid excursion");
        }

        if (ObjectUtils.isEmpty(transport)) {
            throw new InvalidArgumentException("Invalid transport");
        }

        long excursionId = excursion.getId();
        Excursion searchedExcursion = excursionService.findById(excursionId);
        int availableTicketsCount = searchedExcursion.getAvailableTicketsCount();

        if (!areTicketsSufficient(reservedTicketsCount, availableTicketsCount)) {
            throw new InvalidArgumentException("Unavailable tickets count");
        }
    }

    private boolean areTicketsSufficient(int reservedTicketsCount, int availableTicketsCount) {
        return reservedTicketsCount <= availableTicketsCount;
    }

    private void reserveTickets(Booking booking) {
        long excursionId = getExcursionId(booking);
        int ticketsCount = booking.getReservedTicketsCount();

        excursionService.reserveTickets(excursionId, ticketsCount);
    }

    private long getExcursionId(Booking booking) {
        ExcursionTransport excursionTransport = booking.getExcursionTransport();
        Excursion excursion = excursionTransport.getExcursion();

        return excursion.getId();
    }
}