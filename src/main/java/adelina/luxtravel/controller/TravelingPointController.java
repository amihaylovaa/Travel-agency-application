package adelina.luxtravel.controller;

import adelina.luxtravel.domain.TravelingPoint;
import adelina.luxtravel.service.TravelingPointService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Represents controller for a traveling point
 */
@RestController
@RequestMapping("/traveling-points")
public class TravelingPointController {

    private final TravelingPointService travelingPointService;

    @Autowired
    public TravelingPointController(TravelingPointService travelingPointService) {
        this.travelingPointService = travelingPointService;
    }

    @PostMapping
    public TravelingPoint save(@RequestBody TravelingPoint travelingPoint) {
        return travelingPointService.save(travelingPoint);
    }

    @GetMapping(value = "/{id}")
    public TravelingPoint findById(@PathVariable("id") long id) {
        return travelingPointService.findById(id);
    }

    @GetMapping(value = "/all")
    public List<TravelingPoint> findByName(@RequestParam(value = "name") String name) {
        return travelingPointService.findByName(name);
    }

    @GetMapping
    public List<TravelingPoint> findAll() {
        return travelingPointService.findAll();
    }

    @DeleteMapping(value = "/{id}")
    public boolean deleteById(@PathVariable long id) {
        return travelingPointService.deleteById(id);
    }
}