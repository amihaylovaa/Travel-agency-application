package adelina.luxtravel.controller;

import adelina.luxtravel.domain.Booking;
import adelina.luxtravel.exception.InvalidArgumentException;
import adelina.luxtravel.exception.NonExistentItemException;
import adelina.luxtravel.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public Booking save(@RequestBody Booking booking)
            throws InvalidArgumentException, NonExistentItemException {
        return bookingService.save(booking);
    }

    @GetMapping(value = "/id")
    public Booking findById(@PathVariable("id") long id)
            throws InvalidArgumentException, NonExistentItemException {
        return bookingService.findById(id);
    }

    @GetMapping(value = "/bookings-user")
    public List<Booking> findAllUserBookings(@PathVariable("username") String username)
            throws InvalidArgumentException, NonExistentItemException {
        return bookingService.findAllUserBookings(username);
    }

    @GetMapping(value = "/all")
    public List<Booking> findAll() throws NonExistentItemException {
        return bookingService.findAll();
    }

    @PutMapping
    public void updateTickets(@PathVariable("id") long id,
                              @PathVariable("newTicketsCount") int newTicketsCount)
            throws InvalidArgumentException, NonExistentItemException {
        bookingService.updateTickets(id, newTicketsCount);
    }

    @DeleteMapping(value = "id")
    public void deleteById(long id) throws InvalidArgumentException, NonExistentItemException {
        bookingService.deleteById(id);
    }
}