package adelina.luxtravel.controller;

import adelina.luxtravel.domain.TravelingData;
import adelina.luxtravel.domain.transport.Transport;
import adelina.luxtravel.dto.DateDTO;
import adelina.luxtravel.dto.TransportDTO;
import adelina.luxtravel.dto.TravelingDataDTO;
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
    public TravelingData save(@RequestBody TravelingDataDTO travelingDataDTO) {
        return travelingDataService.save(travelingDataDTO);
    }

    @GetMapping(value = "/{id}")
    public TravelingData findById(@PathVariable("id") long id) {
        return travelingDataService.findById(id);
    }

    @GetMapping(value = "/dates")
    public List<TravelingData> findByDates(@RequestBody DateDTO dateDTO) {
        return travelingDataService.findByDates(dateDTO);
    }

    @GetMapping(value = "/all")
    public List<TravelingData> findAll() {
        return travelingDataService.findAll();
    }

    @PutMapping(value = "/{travelingDataId}")
    public void updateTransport(@RequestBody TransportDTO transportDTO,
                                @PathVariable("travelingDataId") long travelingDataId) {
        travelingDataService.updateTransport(travelingDataId, transportDTO);
    }

    // todo check
    @DeleteMapping(value = "/{id}")
    public void deleteById(@PathVariable("id") long id) {
        travelingDataService.deleteById(id);
    }
}