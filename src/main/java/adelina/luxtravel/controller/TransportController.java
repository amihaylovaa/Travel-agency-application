
package adelina.luxtravel.controller;

import adelina.luxtravel.domain.transport.Transport;
import adelina.luxtravel.enumeration.TransportClass;
import adelina.luxtravel.service.TransportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Represents controller for a transport
 */
@RestController
@RequestMapping("/transports")
public class TransportController {

    private final TransportService transportService;

    @Autowired
    public TransportController(TransportService transportService) {
        this.transportService = transportService;
    }

    @PostMapping
    public Transport save(@RequestBody Transport transport) {
        return transportService.save(transport);
    }

    @GetMapping(value = "/{id}")
    public Transport findById(@PathVariable("id") long id) {
        return transportService.findById(id);
    }

    @GetMapping(value = "/specific")
    public List<Transport> findSpecificTransportsByTransportClass(@RequestParam(value = "class") TransportClass transportClass,
                                                                  @RequestParam(value = "type") String type) {
        return transportService.findSpecificTransportsByTransportClass(transportClass, type);
    }

    @GetMapping(value = "/buses")
    public List<Transport> findAllBuses() {
        return transportService.findAllBuses();
    }

    @GetMapping(value = "/airplanes")
    public List<Transport> findAllAirplanes() {
        return transportService.findAllAirplanes();
    }

    @GetMapping
    public List<Transport> findAll() {
        return transportService.findAll();
    }

    @PatchMapping(value = "/{id}/class")
    public Transport updateTransportClass(@PathVariable("id") long id, @RequestBody Transport transport) {
        return transportService.updateTransportClass(id, transport);
    }

    @DeleteMapping(value = "/{id}")
    public boolean deleteById(@PathVariable("id") long id) {
        return transportService.deleteById(id);
    }
}