package adelina.luxtravel.controller;

import adelina.luxtravel.domain.TravelingData;
import adelina.luxtravel.domain.transport.Transport;
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
    public TravelingData save(@RequestBody TravelingData travelingData) {
        return travelingDataService.save(travelingData);
    }

    @GetMapping(value = "/{id}")
    public TravelingData findById(@PathVariable("id") long id) {
        return travelingDataService.findById(id);
    }

    // todo  check
    @GetMapping(value = "/dates/{from}/{to}")
    public List<TravelingData> findByDates(@PathVariable("from") LocalDate from,
                                           @PathVariable("to") LocalDate to) {
        return travelingDataService.findByDates(from, to);
    }

    // todo check
    @GetMapping(value = "/all")
    public List<TravelingData> findAll() {
        return travelingDataService.findAll();
    }

    // todo check
    @PutMapping(value = "/{travelingDataId}")
    public void updateTransport(@RequestBody Transport transport,
                                @PathVariable("travelingDataId") long travelingDataId) {
        travelingDataService.updateTransport(travelingDataId, transport);
    }

    // todo check
    @DeleteMapping(value = "/{id}")
    public void deleteById(@PathVariable("id") long id) {
        travelingDataService.deleteById(id);
    }
}