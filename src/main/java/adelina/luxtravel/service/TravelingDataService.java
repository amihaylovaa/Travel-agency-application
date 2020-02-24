package adelina.luxtravel.service;

import adelina.luxtravel.domain.*;
import adelina.luxtravel.domain.transport.Transport;
import adelina.luxtravel.domain.transport.TransportClass;
import adelina.luxtravel.domain.wrapper.*;
import adelina.luxtravel.exception.*;
import adelina.luxtravel.repository.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static adelina.luxtravel.utility.Constants.INVALID_ID;

@Service
@Transactional
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


    public TravelingData save(TravelingData travelingData) {
        validateTravelingData(travelingData);

        return travelingDataRepository.save(travelingData);
    }

    public TravelingData findById(long id) {
        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException(INVALID_ID);
        }

        Optional<TravelingData> travelingData = travelingDataRepository.findById(id);

        if (!travelingData.isPresent()) {
            throw new NonExistentItemException("This booking data does not exist");
        }
        return travelingData.get();
    }

    public List<TravelingData> findByDates(Date dates) {
        LocalDate from = dates.getFromDate();
        LocalDate to = dates.getToDate();

        validateDates(from, to);

        List<TravelingData> travelingData = travelingDataRepository.findByDates(from, to);

        if (ObjectUtils.isEmpty(travelingData)) {
            throw new NonExistentItemException("There are no booking data for these days");
        }
        return travelingData;
    }

    public List<TravelingData> findAll() {
        List<TravelingData> travelingData = travelingDataRepository.findAll();

        if (ObjectUtils.isEmpty(travelingData)) {
            throw new NonExistentItemException("No booking data found");
        }
        return travelingData;
    }


    public void updateTransport(long travelingDataId, Transport transport) {
        validateUpdateTransportParameters(travelingDataId, transport);

        long transportId = transport.getId();

        travelingDataRepository.updateTransport(transportId, travelingDataId);
    }

    public void deleteById(long id) {
        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException(INVALID_ID);
        }

        if (!travelingDataExists(id)) {
            throw new NonExistentItemException("Traveling data with this id does not exist");
        }
        travelingDataRepository.deleteById(id);
    }

    private void validateFields(DepartureDestination departureDestination, Transport transport, Date date) {
        if (departureDestination == null) {
            throw new InvalidArgumentException("Invalid departure destination");
        }

        if (date == null) {
            throw new InvalidArgumentException("Invalid dates");
        }

        validateTransport(transport);
        validateTransportExists(transport);
        validateDepartureDestinationExists(departureDestination);
        validateDates(date.getFromDate(), date.getToDate());
    }

    private void validateTransportExists(Transport transport) {
        long transportId = transport.getId();
        Optional<Transport> transportOptional = transportRepository.findById(transportId);

        if (!transportOptional.isPresent()) {
            throw new NonExistentItemException("This transport does not exist");
        }
    }

    private void validateDepartureDestinationExists(DepartureDestination departureDestination) {
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
    }

    private void validateUpdateTransportParameters(long travelingDataId, Transport transport) {
        if (travelingDataId <= NumberUtils.INTEGER_ZERO) {
            throw new InvalidArgumentException("Invalid traveling data id");
        }

        if (!travelingDataExists(travelingDataId)) {
            throw new NonExistentItemException("This traveling data does not exist");
        }

        validateTransport(transport);
        validateTransportExists(transport);
    }

    private boolean travelingDataExists(long travelingDataId) {
        Optional<TravelingData> travelingData = travelingDataRepository.findById(travelingDataId);

        return travelingData.isPresent();
    }

    private void validateDates(LocalDate from, LocalDate to) {
        if (from == null || to == null ||
                from.isEqual(to) || from.isAfter(to) || from.isBefore(LocalDate.now())) {
            throw new InvalidArgumentException("Invalid from and to dates");
        }
    }

    private void validateTransport(Transport transport) {
        if (transport == null) {
            throw new InvalidArgumentException("Invalid transport");
        }

        TransportClass transportClass = transport.getTransportClass();

        if (transportClass == null) {
            throw new InvalidArgumentException("Invalid transport class");
        }
    }

    private void validateTravelingData(TravelingData travelingData) {
        if (travelingData == null) {
            throw new InvalidArgumentException("Invalid traveling data");
        }

        Transport transport = travelingData.getTransport();
        DepartureDestination departureDestination = travelingData.getDepartureDestination();
        Date date = travelingData.getDate();
        int availableTicketsCount = travelingData.getAvailableTicketsCount();

        validateFields(departureDestination, transport, date);

        if (availableTicketsCount <= NumberUtils.INTEGER_ZERO) {
            throw new InvalidArgumentException("Invalid tickets count");
        }
    }
}