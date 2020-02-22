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
    public Booking save(@RequestBody Booking booking) {
        return bookingService.save(booking);
    }

    @GetMapping(value = "/{id}")
    public Booking findById(@PathVariable("id") long id) {
        return bookingService.findById(id);
    }

    @GetMapping(value = "/user/{username}")
    public List<Booking> findAllUserBookings(@PathVariable("username") String username){
        return bookingService.findAllUserBookings(username);
    }

    @GetMapping(value = "/all")
    public List<Booking> findAll()  {
        return bookingService.findAll();
    }

    @PutMapping(value = "/{id}/{reservedTicketsCount}")
    public void updateTickets(@PathVariable("id") long id,
                              @PathVariable("reservedTicketsCount") int reservedTicketsCount)
          {
        bookingService.updateTickets(id, reservedTicketsCount);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteById(@PathVariable ("id") long id)  {
        bookingService.deleteById(id);
    }
}