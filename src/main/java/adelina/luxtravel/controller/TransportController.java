package adelina.luxtravel.controller;

import adelina.luxtravel.domain.transport.Transport;
import adelina.luxtravel.domain.transport.TransportClass;
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

    @PostMapping
    public Transport save(@RequestBody Transport transport)
            throws InvalidArgumentException {
        return transportService.save(transport);
    }

    @PostMapping(value = "/all")
    public List<Transport> saveAll(@RequestBody List<Transport> transports)
            throws InvalidArgumentException {
        return transportService.saveAll(transports);
    }

    @GetMapping(value = "/id")
    public Transport findById(@PathVariable("id") long id)
            throws InvalidArgumentException, NonExistentItemException {
        return transportService.findById(id);
    }

    @GetMapping(value = "/all-buses-by-transport_class")
    public List<Transport> findAllBusesByClass(@PathVariable("transportClass") TransportClass transportClass)
            throws InvalidArgumentException, NonExistentItemException {
        return transportService.findAllBusesByClass(transportClass);
    }

    @GetMapping(value = "/all-airplanes-by-transport_class")
    public List<Transport> findAllAirplanesByClass(@PathVariable("transportClass") TransportClass transportClass)
            throws InvalidArgumentException, NonExistentItemException {
        return transportService.findAllAirplanesByClass(transportClass);
    }

    @GetMapping(value = "/all-buses")
    public List<Transport> findAllBuses() throws NonExistentItemException {
        return transportService.findAllBuses();
    }

    @GetMapping(value = "/all-airplanes")
    public List<Transport> findAllAirplanes() throws NonExistentItemException {
        return transportService.findAllAirplanes();
    }

    @PutMapping
    public void updateClass(@PathVariable("transportClass") TransportClass transportClass,
                            @PathVariable("id") long id) throws InvalidArgumentException {
        transportService.updateClass(transportClass, id);
    }

    @DeleteMapping
    public void deleteById(@PathVariable("id") long id)
            throws InvalidArgumentException, NonExistentItemException {
        transportService.deleteById(id);
    }
}