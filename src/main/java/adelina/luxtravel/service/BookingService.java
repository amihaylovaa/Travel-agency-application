package adelina.luxtravel.service;

import adelina.luxtravel.domain.Booking;
import adelina.luxtravel.domain.BookingData;
import adelina.luxtravel.domain.User;
import adelina.luxtravel.exception.InvalidArgumentException;
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

    void save(Booking booking) {
        bookingRepository.save(booking);
    }

    Booking findBookingById(long id) {
        return bookingRepository.findBookingById(id);
    }

    List<Booking> findAllBookingsByUserName(String username) {
        return bookingRepository.findAllBookingsByUsername(username);
    }

    void deleteBooking(long id) {
        bookingRepository.deleteBooking(id);
    }

    void deleteBooking(Booking booking) {
        bookingRepository.delete(booking);
    }

    void deleteAllBookings() {
        bookingRepository.deleteAll();
    }

    void validateBooking(Booking booking) {
        if (booking == null) {
            throw new InvalidArgumentException("Invalid booking");
        }
    }
}
