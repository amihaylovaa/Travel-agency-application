package adelina.luxtravel.service;

import adelina.luxtravel.domain.Booking;
import adelina.luxtravel.domain.BookingData;
import adelina.luxtravel.domain.User;
import adelina.luxtravel.exception.InvalidArgumentException;
import adelina.luxtravel.repository.BookingDataRepository;
import adelina.luxtravel.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class BookingService {
    private BookingRepository bookingRepository;
    private BookingDataRepository bookingDataRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, BookingDataRepository bookingDataRepository) {
        this.bookingRepository = bookingRepository;
        this.bookingDataRepository = bookingDataRepository;
    }

    public void save(Booking booking) {
        validateBooking(booking);
        bookingRepository.decrementCountAvailableTickets(booking.getCountAvailableTickets(), booking.getId());
        bookingRepository.save(booking);
    }

    public Booking findBookingById(long id) {
        if (id <= 0) {
            throw new InvalidArgumentException("Invalid id");
        }
        return bookingRepository.findById(id);
    }

    public List<Booking> findAllBookingsByUserName(String username) {
        if (StringUtils.isEmpty(username)) {
            throw new InvalidArgumentException("Invalid username");
        }
        return bookingRepository.findAllBookingsByUsername(username);
    }

    public void deleteBooking(long id) {
        if (id <= 0) {
            throw new InvalidArgumentException("Invalid id");
        }
        bookingRepository.deleteById(id);
    }

    private void validateBooking(Booking booking) {
        if (booking == null) {
            throw new InvalidArgumentException("Invalid booking");
        }
        User user = booking.getUser();
        BookingData bookingData = booking.getBookingData();
        double price = booking.getPrice();

        if (user == null || bookingData == null || price <= 0.00) {
            throw new InvalidArgumentException("Invalid booking fields");
        }
    }
}