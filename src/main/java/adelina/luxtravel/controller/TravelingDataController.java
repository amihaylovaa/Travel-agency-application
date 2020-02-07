package adelina.luxtravel.controller;

import adelina.luxtravel.domain.TravelingData;
import adelina.luxtravel.domain.transport.Transport;
import adelina.luxtravel.exception.InvalidArgumentException;
import adelina.luxtravel.exception.NonExistentItemException;
import adelina.luxtravel.service.TravelingDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/traveling_data")
public class TravelingDataController {
    private TravelingDataService travelingDataService;

    @Autowired
    public TravelingDataController(TravelingDataService travelingDataService) {
        this.travelingDataService = travelingDataService;
    }

    @PostMapping
    public TravelingData save(@RequestBody TravelingData travelingData)
            throws InvalidArgumentException, NonExistentItemException {
        return travelingDataService.save(travelingData);
    }

    @GetMapping(value = "/{id}")
    public TravelingData findById(@PathVariable("id") long id)
            throws InvalidArgumentException, NonExistentItemException {
        return travelingDataService.findById(id);
    }

    @GetMapping(value = "/dates")
    public List<TravelingData> findByDates(@RequestBody LocalDate from, @RequestBody LocalDate to)
            throws InvalidArgumentException, NonExistentItemException {
        return travelingDataService.findByDates(from, to);
    }

    @GetMapping(value = "/all")
    public List<TravelingData> findAll() throws NonExistentItemException {
        return travelingDataService.findAll();
    }

    @PutMapping(value = "/{id}")
    public void updateTransport(@PathVariable("bookingDataId") long travelingDataId,
                                @RequestBody Transport transport)
            throws InvalidArgumentException, NonExistentItemException {
        travelingDataService.updateTransport(travelingDataId, transport);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteById(@PathVariable("id") long id) throws InvalidArgumentException {
        travelingDataService.deleteById(id);
    }
}