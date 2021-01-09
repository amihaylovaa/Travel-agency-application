package adelina.luxtravel.controller;

import adelina.luxtravel.domain.Booking;

import adelina.luxtravel.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Represents controller for a booking
 */
@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public Booking save(@RequestBody Booking booking) {
        return bookingService.save(booking);
    }

    @GetMapping(value = "/{id}")
    public Booking findById(@PathVariable("id") long id) {
        return bookingService.findById(id);
    }

    @GetMapping(value = "/user/{username}")
    public List<Booking> findAllUserBookings(@PathVariable("username") String username) {
        return bookingService.findAllUserBookings(username);
    }

    @GetMapping
    public List<Booking> findAll() {
        return bookingService.findAll();
    }

    @PatchMapping(value = "/{id}/tickets-count/{reservedTicketsCount}")
    public Booking updateReservedTicketsCount(@PathVariable("id") long id,
                                              @PathVariable("reservedTicketsCount") int reservedTicketsCount) {
        return bookingService.updateReservedTicketsCount(id, reservedTicketsCount);
    }

    @DeleteMapping(value = "/{id}")
    public boolean deleteById(@PathVariable("id") long id) {
        return bookingService.deleteById(id);
    }
}