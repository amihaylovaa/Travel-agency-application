package adelina.luxtravel.service;

import adelina.luxtravel.domain.BookingData;
import adelina.luxtravel.domain.TravelingPoint;
import adelina.luxtravel.exception.InvalidArgumentException;
import adelina.luxtravel.repository.BookingDataRepository;
import adelina.luxtravel.repository.TransportRepository;
import adelina.luxtravel.repository.TravelingPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookingDataService {
    private BookingDataRepository bookingDataRepository;
    private TransportRepository transportRepository;
    private TravelingPointRepository travelingPointRepository;

    public BookingDataService(BookingDataRepository bookingDataRepository, TransportRepository transportRepository, TravelingPointRepository travelingPointRepository) {
        this.bookingDataRepository = bookingDataRepository;
        this.transportRepository = transportRepository;
        this.travelingPointRepository = travelingPointRepository;
    }

    @Autowired
    BookingData findBookingDataById(long id) {
        return bookingDataRepository.findBookingDataById(id);
    }

    List<BookingData> findBookingDataByDates(LocalDate from, LocalDate to) {
        validateDates(from, to);
        return bookingDataRepository.findBookingsDataByDates(from, to);
    }

    BookingData findBookingDataBySourceId(String sourceName) {
        if (sourceName == null || sourceName.isEmpty()) {
            throw new InvalidArgumentException("Invalid source name");
        }
        TravelingPoint travelingPoint = travelingPointRepository.findTravelingPoint(sourceName);
        long id = travelingPoint.getId();

        return bookingDataRepository.findBookingDataBySourceId(id);
    }

    BookingData findBookingDataByDestinationId(String destinationName) {
        if (destinationName == null || destinationName.isEmpty()) {
            throw new InvalidArgumentException("Invalid destination name");
        }
        TravelingPoint travelingPoint = travelingPointRepository.findTravelingPoint(destinationName);
        long id = travelingPoint.getId();

        return bookingDataRepository.findBookingDataByDestinationId(id);
    }

    void validateDates(LocalDate from, LocalDate to) {
        if (from == null || to == null || from.isEqual(to) || from.isAfter(to)) {
            throw new InvalidArgumentException("Invalid dates");
        }
    }
}
