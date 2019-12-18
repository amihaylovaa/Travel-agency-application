package adelina.luxtravel.controller;

import adelina.luxtravel.domain.BookingData;
import adelina.luxtravel.domain.transport.Transport;
import adelina.luxtravel.exception.InvalidArgumentException;
import adelina.luxtravel.exception.NonExistentItemException;
import adelina.luxtravel.service.BookingDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/booking_data")
public class BookingDataController {
    private BookingDataService bookingDataService;

    @Autowired
    public BookingDataController(BookingDataService bookingDataService) {
        this.bookingDataService = bookingDataService;
    }

    @PostMapping
    public BookingData save(@RequestBody BookingData bookingData)
            throws InvalidArgumentException, NonExistentItemException {
        return bookingDataService.save(bookingData);
    }

    @GetMapping(value = "id")
    public BookingData findById(@PathVariable("id") long id)
            throws InvalidArgumentException, NonExistentItemException {
        return bookingDataService.findById(id);
    }

    @GetMapping
    public List<BookingData> findByDates(@PathVariable("from") LocalDate from, @PathVariable("to") LocalDate to)
            throws InvalidArgumentException, NonExistentItemException {
        return bookingDataService.findByDates(from, to);
    }

    @GetMapping
    public List<BookingData> findAll() throws NonExistentItemException {
        return bookingDataService.findAll();
    }

    @PutMapping
    public void updateTransport(@PathVariable("bookingDataId") long bookingDataId,
                                @RequestBody Transport transport)
            throws InvalidArgumentException, NonExistentItemException {
        bookingDataService.updateTransport(bookingDataId, transport);
    }

    @DeleteMapping
    public void deleteById(@PathVariable("id") long id) throws InvalidArgumentException {
        bookingDataService.deleteById(id);
    }
}