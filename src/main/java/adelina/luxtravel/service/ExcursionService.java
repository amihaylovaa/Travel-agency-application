package adelina.luxtravel.service;

import adelina.luxtravel.domain.*;
import adelina.luxtravel.domain.transport.Transport;
import adelina.luxtravel.domain.wrapper.*;
import adelina.luxtravel.exception.*;
import adelina.luxtravel.repository.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static adelina.luxtravel.utility.Constants.*;

/**
 * Represents service for an excursion
 */
@Service
public class ExcursionService {

    private final ExcursionRepository excursionRepository;
    private final ExcursionTransportRepository excursionTransportRepository;
    private final TransportService transportService;
    private final TravelingPointService travelingPointService;

    @Autowired
    public ExcursionService(ExcursionRepository excursionRepository,
                            ExcursionTransportRepository excursionTransportRepository,
                            TransportService transportService,
                            TravelingPointService travelingPointService) {
        this.excursionRepository = excursionRepository;
        this.excursionTransportRepository = excursionTransportRepository;
        this.transportService = transportService;
        this.travelingPointService = travelingPointService;
    }

    /**
     * Saves new excursion
     *
     * @param excursion excursion that is going to be saved
     * @return the saved excursion
     */
    public Excursion save(Excursion excursion) {
        validateExcursion(excursion);
        Excursion savedExcursion = excursionRepository.save(excursion);
        List<ExcursionTransport> excursionTransports = excursion.getExcursionTransports();

        for (ExcursionTransport excursionTransport : excursionTransports) {
            Transport fkTransport = excursionTransport.getTransport();
            double price = excursion.setPrice(fkTransport);

            excursionTransport.setPrice(price);
            excursionTransport.setExcursion(savedExcursion);
            excursionTransport.setTransport(fkTransport);

            excursionTransportRepository.save(excursionTransport);
        }
        return savedExcursion;
    }

    /**
     * Gets excursion by id
     *
     * @param id excursion's id
     * @return if present - the searched excursion, otherwise throws exception for non-existent excursion
     */
    public Excursion findById(long id) {
        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException(INVALID_ID);
        }

        Optional<Excursion> excursion = excursionRepository.findById(id);

        if (!excursion.isPresent()) {
            throw new NonExistentItemException(NON_EXISTENT_EXCURSION);
        }
        return excursion.get();
    }

    /**
     * Gets excursions by dates
     *
     * @return list of all excursion for these dates, throws exception if excursions are not found
     */
    public List<Excursion> findByDates(LocalDate from, LocalDate to) {
        validateDates(from, to);

        List<Excursion> excursion = excursionRepository.findByDates(from, to);

        if (ObjectUtils.isEmpty(excursion)) {
            throw new NonExistentItemException("There are no excursions for these dates");
        }
        return excursion;
    }

    /**
     * Gets the available tickets for a given excursion by id
     *
     * @param id excursion's id
     * @return the available tickets
     */
    public int findExcursionAvailableTicketsCountById(long id) {
        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException(INVALID_ID);
        }

        return excursionRepository.findExcursionAvailableTicketsCountById(id);
    }

    /**
     * Gets all excursions
     *
     * @return list of all excursions, throws exception if excursions are not found
     */
    public List<Excursion> findAll() {
        List<Excursion> excursions = excursionRepository.findAll();

        if (ObjectUtils.isEmpty(excursions)) {
            throw new NonExistentItemException("No excursions found");
        }
        return excursions;
    }

    /**
     * Updates excursion's dates
     *
     * @param id    excursion's id
     * @param dates the new dates for the excursion
     * @return the updated excursion
     */
    public Excursion updateDates(long id, Date dates) {
        validateUpdateDatesParameters(id, dates);

        LocalDate fromDate = dates.getFromDate();
        LocalDate toDate = dates.getToDate();

        excursionRepository.updateFromDate(fromDate, id);
        excursionRepository.updateToDate(toDate, id);

        return findById(id);
    }

    /**
     * Updates tickets count when a booking for a given excursion is made
     *
     * @param excursionId          excursion's id
     * @param reservedTicketsCount the count of reserved tickets
     * @return the available tickets for the excursion after the booking
     */
    public int reserveTickets(long excursionId, int reservedTicketsCount) {
        if (excursionId <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException(INVALID_ID);
        }

        excursionRepository.reserveTickets(reservedTicketsCount, excursionId);

        return findExcursionAvailableTicketsCountById(excursionId);
    }

    /**
     * Updates tickets count when a booking for particular excursion is canceled
     *
     * @param id                   id of the excursion that has been previously booked
     * @param reservedTicketsCount the number of reserved tickets
     * @return the available tickets for the after canceling the booking
     */
    public int cancelTicketsReservation(long id, int reservedTicketsCount) {
        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException(INVALID_ID);
        }

        Optional<Excursion> excursion = excursionRepository.findById(id);

        if (!excursion.isPresent()) {
            throw new NonExistentItemException(NON_EXISTENT_EXCURSION);
        }

        excursionRepository.cancelTicketReservation(reservedTicketsCount, id);

        return findExcursionAvailableTicketsCountById(id);
    }

    /**
     * Deletes excursion by id
     *
     * @param id excursion's id
     * @return true for successfully deleted excursion, false for unsuccessful attempt
     */
    public boolean deleteById(long id) {
        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException(INVALID_ID);
        }

        if (!excursionExists(id)) {
            throw new NonExistentItemException("Excursion with this id does not exist");
        }
        excursionRepository.deleteById(id);

        try {
            findById(id);
        } catch (NonExistentItemException e) {
            return true;
        }
        return false;
    }

    private void validateExcursionFields(DepartureDestination departureDestination, List<Transport> transports, Date date) {
        if (ObjectUtils.isEmpty(departureDestination) || !ObjectUtils.allNotNull(departureDestination)) {
            throw new InvalidArgumentException("Invalid departure destination points");
        }

        if (ObjectUtils.isEmpty(date) || !ObjectUtils.allNotNull(date)) {
            throw new InvalidArgumentException("Invalid dates");
        }

        if (ObjectUtils.isEmpty(transports) || !ObjectUtils.allNotNull(transports)) {
            throw new InvalidArgumentException("Invalid transport");
        }

        validateTravelingPointExist(departureDestination);
        validateTransportsExist(transports);
        validateDates(date.getFromDate(), date.getToDate());
    }

    private void validateTransportsExist(List<Transport> transports) {
        for (Transport transport : transports) {
            long transportId = transport.getId();

            transportService.validateTransportExistById(transportId);
        }
    }

    private void validateTravelingPointExist(DepartureDestination departureDestination) {
        TravelingPoint departurePoint = departureDestination.getDeparturePoint();
        TravelingPoint destinationPoint = departureDestination.getDestinationPoint();
        long departurePointId = departurePoint.getId();
        long destinationPointId = destinationPoint.getId();

        travelingPointService.findById(departurePointId);
        travelingPointService.findById(destinationPointId);
    }

    private void validateUpdateDatesParameters(long excursionId, Date newDates) {
        if (excursionId <= NumberUtils.INTEGER_ZERO) {
            throw new InvalidArgumentException("Invalid excursion's id");
        }

        if (ObjectUtils.isEmpty(newDates)) {
            throw new InvalidArgumentException("Invalid starting and ending dates");
        }

        Excursion excursion = findById(excursionId);

        validateDates(newDates.getFromDate(), newDates.getToDate());

        if (newDates.equals(excursion.getDate())) {
            throw new AlreadyExistingItemException("New dates can not be the same as the current");
        }
    }

    private boolean excursionExists(long excursionId) {
        Optional<Excursion> excursion = excursionRepository.findById(excursionId);

        return excursion.isPresent();
    }

    private void validateDates(LocalDate from, LocalDate to) {
        if (from == null || to == null ||
                from.isEqual(to) || from.isAfter(to) || from.isBefore(LocalDate.now())) {
            throw new InvalidArgumentException("Invalid starting and ending dates");
        }
    }

    private void validateExcursion(Excursion excursion) {
        if (ObjectUtils.isEmpty(excursion)) {
            throw new InvalidArgumentException("Invalid excursion");
        }

        List<ExcursionTransport> excursionTransports = excursion.getExcursionTransports();
        List<Transport> transports = new ArrayList<>();

        for (ExcursionTransport excursionTransport : excursionTransports) {
            Transport transport = excursionTransport.getTransport();
            transports.add(transport);
        }

        DepartureDestination departureDestination = excursion.getDepartureDestination();
        Date date = excursion.getDate();
        int availableTicketsCount = excursion.getAvailableTicketsCount();

        validateExcursionFields(departureDestination, transports, date);

        if (availableTicketsCount <= NumberUtils.INTEGER_ZERO) {
            throw new InvalidArgumentException(INVALID_TICKETS_COUNT);
        }
    }
}