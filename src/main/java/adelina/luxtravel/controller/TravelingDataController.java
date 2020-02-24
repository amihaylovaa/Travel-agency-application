package adelina.luxtravel.controller;

import adelina.luxtravel.domain.TravelingData;
import adelina.luxtravel.domain.transport.Transport;
import adelina.luxtravel.domain.wrapper.Date;
import adelina.luxtravel.service.TravelingDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        return travelingDataService.save(new TravelingData(travelingData));
    }

    @GetMapping(value = "/{id}")
    public TravelingData findById(@PathVariable("id") long id) {
        return travelingDataService.findById(id);
    }

    @GetMapping(value = "/dates")
    public List<TravelingData> findByDates(@RequestBody Date date) {
        return travelingDataService.findByDates(date);
    }

    @GetMapping(value = "/all")
    public List<TravelingData> findAll() {
        return travelingDataService.findAll();
    }

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