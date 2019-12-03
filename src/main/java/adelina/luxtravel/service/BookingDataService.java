package adelina.luxtravel.service;

import adelina.luxtravel.domain.BookingData;
import adelina.luxtravel.domain.TravelingPoint;
import adelina.luxtravel.domain.transport.Transport;
import adelina.luxtravel.exception.InvalidArgumentException;
import adelina.luxtravel.exception.NonExistentItemException;
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

    public BookingData findBookingDataById(long id) {
        if (id <= 0) {
            throw new InvalidArgumentException("Invalid id");
        }
        return bookingDataRepository.findBookingDataById(id);
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

        return bookingDataRepository.findBookingDataBySourceId(travelingPoint.getId());
    }

    public BookingData findBookingDataByDestinationId(String destinationName) {
        if (StringUtils.isEmpty(destinationName)) {
            throw new InvalidArgumentException("Invalid destination name");
        }

        TravelingPoint travelingPoint = travelingPointRepository.findByName(destinationName);

        if (travelingPoint == null) {
            throw new NonExistentItemException("Traveling point does not exist");
        }

        return bookingDataRepository.findBookingDataByDestinationId(travelingPoint.getId());
    }

    public void updateTransport(long bookingDataId, Transport transport) {
        if (bookingDataId <= 0 || transport == null) {
            throw new InvalidArgumentException("Update can not be executed, invalid parameters");
        }

        long transportId = transport.getId();

        if (transportId <= 0) {
            throw new NonExistentItemException("Transport does not exist");
        }

        bookingDataRepository.updateTransport(transportId, bookingDataId);
    }

    public void deleteBookingDataById(long id) {
        if (id <= 0) {
            throw new InvalidArgumentException("Invalid id");
        }
        bookingDataRepository.deleteBookingDataById(id);
    }

    private void validateDates(LocalDate from, LocalDate to) {
        if (from == null || to == null || from.isEqual(to) || from.isAfter(to)) {
            throw new InvalidArgumentException("Invalid dates");
        }
    }
}
