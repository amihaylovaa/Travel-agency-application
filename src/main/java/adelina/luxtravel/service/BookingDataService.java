package adelina.luxtravel.service;

import adelina.luxtravel.domain.BookingData;
import adelina.luxtravel.domain.TravelingPoint;
import adelina.luxtravel.domain.transport.Transport;
import adelina.luxtravel.domain.wrapper.*;
import adelina.luxtravel.exception.*;
import adelina.luxtravel.repository.BookingDataRepository;
import adelina.luxtravel.repository.TransportRepository;
import adelina.luxtravel.repository.TravelingPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
        return save(bookingData);
    }

    public BookingData findBookingDataById(long id) {
        if (id <= 0) {
            throw new InvalidArgumentException("Invalid id");
        }
        return getExistingData(bookingDataRepository.findBookingDataById(id));
    }

    public List<BookingData> findBookingDataByDates(LocalDate from, LocalDate to) {
        validateDates(from, to);
        return bookingDataRepository.findBookingsDataByDates(from, to);
    }

    public BookingData findBookingDataBySourceId(String sourceName) {
        if (StringUtils.isEmpty(sourceName)) {
            throw new InvalidArgumentException("Invalid source name");
        }

        TravelingPoint travelingPoint = travelingPointRepository.findByName(sourceName);

        if (travelingPoint == null) {
            throw new NonExistentItemException("Traveling point does not exist");
        }

        long id = travelingPoint.getId();

        return getExistingData(bookingDataRepository.findBookingDataBySourceId(id));
    }

    public BookingData findBookingDataByDestinationId(String destinationName) {
        if (StringUtils.isEmpty(destinationName)) {
            throw new InvalidArgumentException("Invalid destination name");
        }

        TravelingPoint travelingPoint = travelingPointRepository.findByName(destinationName);

        if (travelingPoint == null) {
            throw new NonExistentItemException("Traveling point does not exist");
        }

        long id = travelingPoint.getId();

        return getExistingData(bookingDataRepository.findBookingDataByDestinationId(id));
    }

    public void updateTransport(long bookingDataId, Transport transport) {
        if (bookingDataId <= 0 || transport == null) {
            throw new InvalidArgumentException("Update can not be executed, invalid parameters");
        }

        long transportId = transport.getId();

        if (transportId <= 0 || transportRepository.findById(transportId) == null) {
            throw new NonExistentItemException("Transport does no exist");
        }
        bookingDataRepository.updateTransport(transportId, bookingDataId);
    }

    public void deleteBookingDataById(long id) {
        if (id <= 0) {
            throw new InvalidArgumentException("Invalid id");
        }
        findBookingDataById(id);
        bookingDataRepository.deleteBookingDataById(id);
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
        SourceDestination sourceDestination = bookingData.getSourceDestination();
        Transport transport = bookingData.getTransport();

        validateDates(from, to);

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

        if (transportRepository.findById(sourceId) == null
                || travelingPointRepository.findById(destinationId) == null
                || transportRepository.findById(transportId) == null) {
            throw new NonExistentItemException("These fields do not exist");
        }
    }

    private void validateDates(LocalDate from, LocalDate to) {
        if (from == null || to == null || from.isEqual(to) || from.isAfter(to)) {
            throw new InvalidArgumentException("Invalid dates");
        }
    }

    private BookingData getExistingData(BookingData bookingData) {
        if (bookingData == null) {
            throw new NonExistentItemException("This booking data does not exist");
        }
        return bookingData;
    }
}