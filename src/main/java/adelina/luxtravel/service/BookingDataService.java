package adelina.luxtravel.service;

import adelina.luxtravel.domain.BookingData;
import adelina.luxtravel.domain.TravelingPoint;
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

@Service
public class BookingDataService {
    private BookingDataRepository bookingDataRepository;
    private TransportRepository transportRepository;
    private TravelingPointRepository travelingPointRepository;

    @Autowired
    public BookingDataService(BookingDataRepository bookingDataRepository, TransportRepository transportRepository, TravelingPointRepository travelingPointRepository) {
        this.bookingDataRepository = bookingDataRepository;
        this.transportRepository = transportRepository;
        this.travelingPointRepository = travelingPointRepository;
    }

    public BookingData save(BookingData bookingData) {
        validateBookingData(bookingData);
        return bookingDataRepository.save(bookingData);
    }

    public BookingData findById(long id) {
        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException("Invalid id");
        }
        return getExistingBookingData(id);
    }

    public List<BookingData> findByDates(LocalDate from, LocalDate to) {
        validateDates(from, to);

        List<BookingData> bookingData = bookingDataRepository.findByDates(from, to);

        if (ObjectUtils.isEmpty(bookingData)) {
            throw new NonExistentItemException("There no booking data for these days");
        }
        return bookingData;
    }

    public List<BookingData> findAll() {
        List<BookingData> bookingData = bookingDataRepository.findAll();

        if (ObjectUtils.isEmpty(bookingData)) {
            throw new NonExistentItemException("No booking data found");
        }
        return bookingData;
    }

    public void updateTransport(long bookingDataId, Transport transport) {
        if (bookingDataId <= NumberUtils.LONG_ZERO || transport == null || transport.getId() <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException("Update can not be executed, invalid parameters");
        }
        bookingDataRepository.updateTransport(transport.getId(), bookingDataId);
    }

    public void deleteBookingDataById(long id) {
        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException("Invalid id");
        }
        findById(id);
        bookingDataRepository.deleteBookingDataById(id);
    }

    public void deleteAll() {
        findAll();
        bookingDataRepository.deleteAll();
    }

    private void validateBookingData(BookingData bookingData) {
        if (bookingData == null) {
            throw new InvalidArgumentException("Invalid booking data");
        }
        validateBookingDataFields(bookingData);
    }

    private void validateBookingDataFields(BookingData bookingData) {
        Date date = bookingData.getDate();
        LocalDate from = date.getFromDate();
        LocalDate to = date.getToDate();

        validateDates(from, to);

        SourceDestination sourceDestination = bookingData.getSourceDestination();
        Transport transport = bookingData.getTransport();

        if (sourceDestination == null || transport == null) {
            throw new InvalidArgumentException("Invalid fields");
        }

        validateFieldsExist(sourceDestination, transport);
    }

    private void validateFieldsExist(SourceDestination sourceDestination, Transport transport) {
        TravelingPoint source = sourceDestination.getSource();
        TravelingPoint destination = sourceDestination.getDestination();
        long sourceId = source.getId();
        long destinationId = destination.getId();
        long transportId = transport.getId();

        if (travelingPointRepository.findById(sourceId) == null
                || travelingPointRepository.findById(destinationId) == null
                || transportRepository.findById(transportId) == null) {
            throw new NonExistentItemException("Booking data fields do not exist");
        }
    }

    private void validateDates(LocalDate from, LocalDate to) {
        if (from == null || to == null || from.isEqual(to)
                || from.isAfter(to) || from.isBefore(LocalDate.now())) {
            throw new InvalidArgumentException("Invalid dates");
        }
    }

    private BookingData getExistingBookingData(long id) {
        BookingData bookingData = bookingDataRepository.findById(id);

        if (bookingData == null) {
            throw new NonExistentItemException("This booking data does not exist");
        }
        return bookingData;
    }

    private long getTravelingPointId(TravelingPoint travelingPoint) {
        if (travelingPoint == null) {
            throw new NonExistentItemException("Traveling point does not exist");
        }
        return travelingPoint.getId();
    }
}