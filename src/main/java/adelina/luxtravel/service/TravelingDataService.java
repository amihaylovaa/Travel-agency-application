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
import java.util.List;
import java.util.Optional;

import static adelina.luxtravel.utility.Constants.INVALID_ID;

@Service
public class TravelingDataService {
    private TravelingDataRepository travelingDataRepository;
    private TransportRepository transportRepository;
    private TravelingPointRepository travelingPointRepository;

    @Autowired
    public TravelingDataService(TravelingDataRepository travelingDataRepository,
                                TransportRepository transportRepository,
                                TravelingPointRepository travelingPointRepository) {
        this.travelingDataRepository = travelingDataRepository;
        this.transportRepository = transportRepository;
        this.travelingPointRepository = travelingPointRepository;
    }

    public TravelingData save(TravelingData travelingData) throws InvalidArgumentException, NonExistentItemException {
        validateBookingData(travelingData);
        validateFieldsExist(travelingData.getDepartureDestination(), travelingData.getTransport());
        return travelingDataRepository.save(travelingData);
    }

    public TravelingData findById(long id) throws InvalidArgumentException, NonExistentItemException {
        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException(INVALID_ID);
        }

        Optional<TravelingData> bookingData = travelingDataRepository.findById(id);

        if (!bookingData.isPresent()) {
            throw new NonExistentItemException("This booking data does not exist");
        }
        return bookingData.get();
    }

    public List<TravelingData> findByDates(LocalDate from, LocalDate to)
            throws InvalidArgumentException, NonExistentItemException {
        validateDates(from, to);

        List<TravelingData> travelingData = travelingDataRepository.findByDates(from, to);

        if (ObjectUtils.isEmpty(travelingData)) {
            throw new NonExistentItemException("There are no booking data for these days");
        }
        return travelingData;
    }

    public List<TravelingData> findAll() throws NonExistentItemException {
        List<TravelingData> travelingData = travelingDataRepository.findAll();

        if (ObjectUtils.isEmpty(travelingData)) {
            throw new NonExistentItemException("No booking data found");
        }
        return travelingData;
    }

    public void updateTransport(long travelingDataId, Transport transport) throws InvalidArgumentException {
        if (travelingDataId <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException("Invalid traveling data id");
        }

        try {
            findById(travelingDataId);
            try {
                validateTransportData(transport);
            }
            catch (NonExistentItemException nonExistentItemException) {
                // TODO : logger
            }
        } catch (NonExistentItemException nonExistentItemException) {
            // TODO : logger
        }

        long transportId = transport.getId();

        travelingDataRepository.updateTransport(transportId, travelingDataId);
    }

    public void deleteById(long id) throws InvalidArgumentException {
        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException(INVALID_ID);
        }
        travelingDataRepository.deleteById(id);
    }

    private void validateBookingData(TravelingData travelingData) throws InvalidArgumentException {
        if (travelingData == null) {
            throw new InvalidArgumentException("Invalid booking data");
        }
    }

    private void validateFieldsExist(DepartureDestination departureDestination, Transport transport)
            throws InvalidArgumentException, NonExistentItemException {
        TravelingPoint departurePoint = departureDestination.getDeparturePoint();
        TravelingPoint destinationPoint = departureDestination.getDestinationPoint();
        long departurePointId = departurePoint.getId();
        long destinationPointId = destinationPoint.getId();
        Optional<TravelingPoint> startingPoint = travelingPointRepository.findById(departurePointId);
        Optional<TravelingPoint> endingPoint = travelingPointRepository.findById(destinationPointId);

        if (!startingPoint.isPresent()) {
            throw new NonExistentItemException("Departure traveling point does not exist");
        }
        if (!endingPoint.isPresent()) {
            throw new NonExistentItemException("Destination traveling point does not exist");
        }

        validateTransportData(transport);
    }

    private void validateTransportData(Transport transport)
            throws InvalidArgumentException, NonExistentItemException {
        if (transport == null) {
            throw new InvalidArgumentException("Invalid transport");
        }

        long transportId = transport.getId();
        Optional<Transport> foundTransport = transportRepository.findById(transportId);

        if (!foundTransport.isPresent()) {
            throw new NonExistentItemException("This transport does not exist");
        }
    }

    private void validateDates(LocalDate from, LocalDate to) throws InvalidArgumentException {
        if (from == null || to == null || from.isEqual(to)
                || from.isAfter(to) || from.isBefore(LocalDate.now())) {
            throw new InvalidArgumentException("Invalid dates");
        }
    }
}