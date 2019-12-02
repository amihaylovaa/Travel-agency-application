package adelina.luxtravel.service;

import adelina.luxtravel.domain.Booking;
import adelina.luxtravel.repository.BookingDataRepository;
import adelina.luxtravel.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {
    private BookingRepository bookingRepository;
    private BookingDataRepository bookingDataRepository;

    public BookingService(BookingRepository bookingRepository, BookingDataRepository bookingDataRepository) {
        this.bookingRepository = bookingRepository;
        this.bookingDataRepository = bookingDataRepository;
    }

    @Autowired
    public void save(Booking booking) {
        bookingRepository.save(booking);
    }

    public Booking findBookingById(long id) {
        return bookingRepository.findBookingById(id);
    }

    public List<Booking> findAllBookingsByUserName(String username) {
        return bookingRepository.findAllBookingsByUsername(username);
    }

    public void deleteBooking(long id) {
        bookingRepository.deleteBooking(id);
    }
}
