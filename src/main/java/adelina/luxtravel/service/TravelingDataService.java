package adelina.luxtravel.service;

import adelina.luxtravel.domain.*;
import adelina.luxtravel.domain.transport.Airplane;
import adelina.luxtravel.domain.transport.Bus;
import adelina.luxtravel.domain.transport.Transport;
import adelina.luxtravel.domain.transport.TransportClass;
import adelina.luxtravel.domain.wrapper.*;
import adelina.luxtravel.dto.*;
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

    public TravelingData save(TravelingDataDTO travelingDataDTO) {
        validateDTO(travelingDataDTO);

        TravelingData travelingData = createTravelingDataFromDTO(travelingDataDTO);

        return save(travelingData);
    }

    public TravelingData save(TravelingData travelingData) {
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

    public List<TravelingData> findByDates(DateDTO dateDTO) {
        validateDates(dateDTO);

        LocalDate from = dateDTO.getFromDate();
        LocalDate to = dateDTO.getToDate();
        Date dates = new Date(from, to);

        return findByDates(dates);
    }

    public List<TravelingData> findByDates(Date dates) {
        LocalDate from = dates.getFromDate();
        LocalDate to = dates.getToDate();

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

    public void updateTransport(long travelingDataId, TransportDTO transportDTO) {
        if (travelingDataId <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException(INVALID_ID);
        }

        validateUpdateTransportParameters(travelingDataId, transportDTO);

        Transport transport = createTransportFromDTO(transportDTO);

        updateTransport(travelingDataId, transport);
    }

    public void updateTransport(long travelingDataId, Transport transport) {
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

    private void validateFields(DepartureDestinationDTO departureDestinationDTO, TransportDTO transportDTO, DateDTO dateDTO) {
        if (departureDestinationDTO == null) {
            throw new InvalidArgumentException("Invalid departure destination");
        }

        if (dateDTO == null) {
            throw new InvalidArgumentException("Invalid dates");
        }

        validateTransport(transportDTO);
        validateTransportExists(transportDTO);
        validateDepartureDestinationExists(departureDestinationDTO);
        validateDates(dateDTO);
    }

    private void validateTransportExists(TransportDTO transport) {
        long transportId = transport.getId();
        Optional<Transport> transportOptional = transportRepository.findById(transportId);

        if (!transportOptional.isPresent()) {
            throw new NonExistentItemException("This transport does not exist");
        }
    }

    private void validateDepartureDestinationExists(DepartureDestinationDTO departureDestinationDTO) {
        TravelingPoint departurePoint = departureDestinationDTO.getDeparturePoint();
        TravelingPoint destinationPoint = departureDestinationDTO.getDestinationPoint();
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

    private void validateUpdateTransportParameters(long travelingDataId, TransportDTO transportDTO) {
        if (!travelingDataExists(travelingDataId)) {
            throw new NonExistentItemException("This traveling data does not exist");
        }

        validateTransport(transportDTO);
        validateTransportExists(transportDTO);
    }

    private boolean travelingDataExists(long travelingDataId) {
        Optional<TravelingData> travelingData = travelingDataRepository.findById(travelingDataId);

        return travelingData.isPresent();
    }

    private void validateDates(DateDTO dateDTO) {
        LocalDate from = dateDTO.getFromDate();
        LocalDate to = dateDTO.getToDate();

        if (from == null || to == null ||
                from.isEqual(to) || from.isAfter(to) || from.isBefore(LocalDate.now())) {
            throw new InvalidArgumentException("Invalid dates");
        }
    }

    private void validateTransport(TransportDTO transportDTO) {
        if (transportDTO == null) {
            throw new InvalidArgumentException("Invalid transport");
        }

        TransportClass transportClass = transportDTO.getTransportClass();

        if (transportClass == null) {
            throw new InvalidArgumentException("Invalid transport class");
        }
    }

    private void validateDTO(TravelingDataDTO travelingDataDTO) {
        if (travelingDataDTO == null) {
            throw new InvalidArgumentException("Invalid traveling data");
        }

        TransportDTO transportDTO = travelingDataDTO.getTransportDTO();
        DepartureDestinationDTO departureDestinationDTO = travelingDataDTO.getDepartureDestinationDTO();
        DateDTO dateDTO = travelingDataDTO.getDateDTO();
        int availableTicketsCount = travelingDataDTO.getAvailableTicketsCount();

        validateFields(departureDestinationDTO, transportDTO, dateDTO);

        if (availableTicketsCount <= NumberUtils.INTEGER_ZERO) {
            throw new InvalidArgumentException("Invalid tickets count");
        }
    }

    private TravelingData createTravelingDataFromDTO(TravelingDataDTO travelingDataDTO) {
        TransportDTO transportDTO = travelingDataDTO.getTransportDTO();
        DepartureDestinationDTO departureDestinationDTO = travelingDataDTO.getDepartureDestinationDTO();
        TravelingPoint departurePoint = departureDestinationDTO.getDeparturePoint();
        TravelingPoint destinationPoint = departureDestinationDTO.getDestinationPoint();
        DateDTO dateDTO = travelingDataDTO.getDateDTO();
        LocalDate from = dateDTO.getFromDate();
        LocalDate to = dateDTO.getToDate();
        Date date = new Date(from, to);
        DepartureDestination departureDestination = new DepartureDestination(departurePoint, destinationPoint);
        Transport transport = createTransportFromDTO(transportDTO);
        int availableTicketsCount = travelingDataDTO.getAvailableTicketsCount();

        return new TravelingData(transport, departureDestination, date, availableTicketsCount);
    }

    private Transport createTransportFromDTO(TransportDTO transportDTO) {
        long id = transportDTO.getId();
        TransportClass transportClass = transportDTO.getTransportClass();

        if (transportDTO instanceof AirplaneDTO) {
            return new Airplane(id, transportClass);
        }
        return new Bus(id, transportClass);
    }
}