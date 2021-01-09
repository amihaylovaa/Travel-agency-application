package adelina.luxtravel.controller;

import adelina.luxtravel.service.ExcursionTransportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Represents controller for an excursion-transport
 */
@RestController
@RequestMapping(value = "/excursions-transport")
public class ExcursionTransportController {

    private final ExcursionTransportService excursionTransportService;

    @Autowired
    public ExcursionTransportController(ExcursionTransportService excursionTransportService) {
        this.excursionTransportService = excursionTransportService;
    }

    @GetMapping
    public long getExcursionId(@RequestParam(value = "transport-id") long transportId,
                               @RequestParam(value = "price") double price) {
        return excursionTransportService.getExcursionId(transportId, price);
    }
}
