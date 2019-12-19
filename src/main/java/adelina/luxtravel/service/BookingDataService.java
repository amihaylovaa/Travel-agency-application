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

@Service
public class BookingDataService {
    private BookingDataRepository bookingDataRepository;
    private TransportRepository transportRepository;
    private TravelingPointRepository travelingPointRepository;

    @Autowired
    public BookingDataService(BookingDataRepository bookingDataRepository,
                              TransportRepository transportRepository,
                              TravelingPointRepository travelingPointRepository) {
        this.bookingDataRepository = bookingDataRepository;
        this.transportRepository = transportRepository;
        this.travelingPointRepository = travelingPointRepository;
    }

    public BookingData save(BookingData bookingData)
            throws InvalidArgumentException, NonExistentItemException {
        validateBookingData(bookingData);
        return bookingDataRepository.save(bookingData);
    }

    public BookingData findById(long id) throws InvalidArgumentException, NonExistentItemException {
        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException("Invalid id");
        }

        Optional<BookingData> bookingData = bookingDataRepository.findById(id);

        if (!bookingData.isPresent()) {
            throw new NonExistentItemException("This booking data does not exist");
        }
        return bookingData.get();
    }

    public List<BookingData> findByDates(LocalDate from, LocalDate to)
            throws InvalidArgumentException, NonExistentItemException {
        validateDates(from, to);

        List<BookingData> bookingData = bookingDataRepository.findByDates(from, to);

        if (ObjectUtils.isEmpty(bookingData)) {
            throw new NonExistentItemException("There are no booking data for these days");
        }
        return bookingData;
    }

    public List<BookingData> findAll() throws NonExistentItemException {
        List<BookingData> bookingData = bookingDataRepository.findAll();

        if (ObjectUtils.isEmpty(bookingData)) {
            throw new NonExistentItemException("No booking data found");
        }
        return bookingData;
    }

    public void updateTransport(long bookingDataId, Transport transport)
            throws InvalidArgumentException, NonExistentItemException {
        validateTransportData(transport);

        if (bookingDataId <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException("Invalid booking data id");
        }

        long transportId = transport.getId();

        bookingDataRepository.updateTransport(transportId, bookingDataId);
    }

    public void deleteById(long id) throws InvalidArgumentException {
        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException("Invalid id");
        }
        bookingDataRepository.deleteById(id);
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

    private void validateBookingData(BookingData bookingData)
            throws InvalidArgumentException, NonExistentItemException {
        if (bookingData == null) {
            throw new InvalidArgumentException("Invalid booking data");
        }
        validateFields(bookingData);
    }

    private void validateFields(BookingData bookingData)
            throws InvalidArgumentException, NonExistentItemException {
        Date date = bookingData.getDate();
        LocalDate from = date.getFromDate();
        LocalDate to = date.getToDate();

        validateDates(from, to);

        DepartureDestination departureDestination = bookingData.getDepartureDestination();

        if (departureDestination == null) {
            throw new InvalidArgumentException("Invalid traveling points");
        }

        Transport transport = bookingData.getTransport();

        if (transport == null) {
            throw new InvalidArgumentException("Invalid transport");
        }

        TravelingPoint startingPoint = departureDestination.getDeparturePoint();
        TravelingPoint targetPoint = departureDestination.getDestinationPoint();

        validateFieldsExist(startingPoint, targetPoint, transport);
    }

    private void validateFieldsExist(TravelingPoint startingPoint, TravelingPoint targetPoint,
                                     Transport transport) throws NonExistentItemException {
        long transportId = transport.getId();
        long startingPointId = startingPoint.getId();
        long targetPointId = targetPoint.getId();
        Optional<TravelingPoint> fromPoint = travelingPointRepository.findById(startingPointId);
        Optional<TravelingPoint> toPoint = travelingPointRepository.findById(targetPointId);
        Optional<Transport> transports = transportRepository.findById(transportId);

        if (!fromPoint.isPresent()) {
            throw new NonExistentItemException("Source traveling point does not exist");
        }
        if (!toPoint.isPresent()) {
            throw new NonExistentItemException("Destination traveling point does not exist");
        }
        if (!transports.isPresent()) {
            throw new NonExistentItemException("Transport does not exist");
        }
    }

    private void validateDates(LocalDate from, LocalDate to) throws InvalidArgumentException {
        if (from == null || to == null || from.isEqual(to)
                || from.isAfter(to) || from.isBefore(LocalDate.now())) {
            throw new InvalidArgumentException("Invalid dates");
        }
    }
}