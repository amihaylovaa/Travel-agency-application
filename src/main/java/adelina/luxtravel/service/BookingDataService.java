package adelina.luxtravel.service;

import adelina.luxtravel.repository.BookingDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingDataService {
    private BookingDataRepository bookingDataRepository;

    @Autowired
    public BookingDataService(BookingDataRepository bookingDataRepository) {
        this.bookingDataRepository = bookingDataRepository;
    }
}
