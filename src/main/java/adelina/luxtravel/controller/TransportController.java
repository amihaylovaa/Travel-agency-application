package adelina.luxtravel.controller;

import adelina.luxtravel.domain.transport.Transport;
import adelina.luxtravel.domain.transport.TransportClass;
import adelina.luxtravel.dto.TransportDTO;
import adelina.luxtravel.exception.InvalidArgumentException;
import adelina.luxtravel.exception.NonExistentItemException;
import adelina.luxtravel.service.TransportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transports")
public class TransportController {
    private TransportService transportService;

    @Autowired
    public TransportController(TransportService transportService) {
        this.transportService = transportService;
    }

    @PostMapping(value = "/bus")
    public Transport saveBus(@RequestBody TransportDTO transportDTO) {
        return transportService.saveBus(transportDTO);
    }

    @PostMapping(value = "/airplane")
    public Transport saveAirplane(@RequestBody TransportDTO transportDTO) {
        return transportService.saveAirplane(transportDTO);
    }

    @PostMapping(value = "/all")
    public List<Transport> saveAll(@RequestBody List<TransportDTO> transportsDTO) {
        return transportService.saveAllDTO(transportsDTO);
    }

    @GetMapping(value = "/{id}")
    public Transport findById(@PathVariable("id") long id) {
        return transportService.findById(id);
    }

    @GetMapping(value = "/buses/{transportClass}")
    public List<Transport> findAllBusesByClass(@PathVariable TransportClass transportClass) {
        return transportService.findAllBusesByClass(transportClass.name());
    }

    @GetMapping(value = "/airplanes/{transportClass}")
    public List<Transport> findAllAirplanesByClass(@PathVariable String transportClass) {
        return transportService.findAllAirplanesByClass(transportClass);
    }

    @GetMapping(value = "/all/buses")
    public List<Transport> findAllBuses() {
        return transportService.findAllBuses();
    }

    @GetMapping(value = "/all/airplanes")
    public List<Transport> findAllAirplanes() {
        return transportService.findAllAirplanes();
    }

    @PutMapping(value = "/{transportClass}/{id}")
    public void updateClass(@PathVariable String transportClass,
                            @PathVariable("id") long id) {
       transportService.updateClass(transportClass, id);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteById(@PathVariable("id") long id) {
        transportService.deleteById(id);
    }
}