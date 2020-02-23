package adelina.luxtravel.service;

import adelina.luxtravel.domain.*;
import adelina.luxtravel.domain.transport.Airplane;
import adelina.luxtravel.domain.transport.Bus;
import adelina.luxtravel.domain.transport.Transport;
import adelina.luxtravel.domain.transport.TransportClass;
import adelina.luxtravel.domain.wrapper.Date;
import adelina.luxtravel.domain.wrapper.DepartureDestination;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class BookingService {
    private BookingRepository bookingRepository;
    private TravelingDataRepository travelingDataRepository;
    private UserRepository userRepository;
/*
    @Autowired
    public BookingService(BookingRepository bookingRepository, TravelingDataRepository travelingDataRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.travelingDataRepository = travelingDataRepository;
        this.userRepository = userRepository;
    }

    public Booking save(BookingDTO bookingDTO) {
        if (bookingDTO == null) {
            throw new InvalidArgumentException("Invalid booking");
        }

        validateFields(bookingDTO);

        Booking booking = createBookingFromDTO(bookingDTO);

        return save(booking);
    }

    public Booking save(Booking booking) {
        reserveTickets(booking);

        return bookingRepository.save(booking);
    }

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
        if (bookingId <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException(INVALID_ID);
        }
        if (reservedTicketsCount <= NumberUtils.INTEGER_ZERO) {
            throw new InvalidArgumentException("Invalid tickets count");
        }

        validateTicketsUpdateParameters(bookingId, reservedTicketsCount);

        bookingRepository.updateByTickets(reservedTicketsCount, bookingId);
    }

    public void deleteById(long id) {
        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException(INVALID_ID);
        }

        Optional<Booking> booking = bookingRepository.findById(id);

        if (!booking.isPresent()) {
            throw new NonExistentItemException("This booking does not exist");
        }
        cancelTicketsReservation(id);
        bookingRepository.deleteById(id);
    }

    private void validateFields(BookingDTO bookingDTO) {
        User user = bookingDTO.getUser();
        TravelingDataDTO travelingDataDTO = bookingDTO.getTravelingDataDTO();

        if (user == null) {
            throw new InvalidArgumentException("Invalid user");
        }

        if (travelingDataDTO == null) {
            throw new InvalidArgumentException("Invalid traveling data");
        }

        validateUserExists(user.getUsername());
        validateTravelingDataExists(travelingDataDTO);
        validateTicketsAreSufficient(bookingDTO.getReservedTicketsCount(), travelingDataDTO.getAvailableTicketsCount());
    }

    private void validateTravelingDataExists(TravelingDataDTO travelingDataDTO) {
        long id = travelingDataDTO.getId();
        Optional<TravelingData> searchedBookingData = travelingDataRepository.findById(id);

        if (!searchedBookingData.isPresent()) {
            throw new NonExistentItemException("Booking data does not exist");
        }
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

    private Booking createBookingFromDTO(BookingDTO bookingDTO) {
        int reservedTicketsCount = bookingDTO.getReservedTicketsCount();
        User user = bookingDTO.getUser();
        TravelingDataDTO travelingDataDTO = bookingDTO.getTravelingDataDTO();
        TravelingData travelingData = createTravelingDataFromDTO(travelingDataDTO);

        return new Booking(travelingData, user, reservedTicketsCount);
    }

    private TravelingData createTravelingDataFromDTO(TravelingDataDTO travelingDataDTO) {
        long travelingDataId = travelingDataDTO.getId();
        DepartureDestinationDTO departureDestinationDTO = travelingDataDTO.getDepartureDestinationDTO();
        TravelingPoint departurePoint = departureDestinationDTO.getDeparturePoint();
        TravelingPoint destinationPoint = departureDestinationDTO.getDestinationPoint();
        TransportDTO transportDTO = travelingDataDTO.getTransportDTO();
        int availableTicketsCount = travelingDataDTO.getAvailableTicketsCount();
        DateDTO dateDTO = travelingDataDTO.getDateDTO();
        LocalDate from = dateDTO.getFromDate();
        LocalDate to = dateDTO.getToDate();
        Date dates = new Date(from, to);
        DepartureDestination departureDestination = new DepartureDestination(departurePoint, destinationPoint);
        Transport transport = createTransportFromDTO(transportDTO);

        return new TravelingData(travelingDataId, departureDestination, transport, dates, availableTicketsCount);
    }

    private Transport createTransportFromDTO(TransportDTO transportDTO) {
        long transportId = transportDTO.getId();
        TransportClass transportClass = transportDTO.getTransportClass();

        if (transportDTO instanceof AirplaneDTO) {
            return new Airplane(transportId, transportClass);
        } else {
            return new Bus(transportId, transportClass);
        }
    }*/
}