package adelina.luxtravel.controller;

import adelina.luxtravel.domain.TravelingPoint;
import adelina.luxtravel.exception.InvalidArgumentException;
import adelina.luxtravel.exception.NonExistentItemException;
import adelina.luxtravel.service.TravelingPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/traveling_points")
public class TravelingPointController {
    private TravelingPointService travelingPointService;

    @Autowired
    public TravelingPointController(TravelingPointService travelingPointService) {
        this.travelingPointService = travelingPointService;
    }

    @PostMapping
    public TravelingPoint save(@RequestBody TravelingPoint travelingPoint)
            throws InvalidArgumentException, NonExistentItemException {
        return travelingPointService.save(travelingPoint);
    }

    @PostMapping(value = "/list")
    public List<TravelingPoint> saveAll(@RequestBody List<TravelingPoint> travelingPoints)
            throws InvalidArgumentException, NonExistentItemException {
        return travelingPointService.saveAll(travelingPoints);
    }

    @GetMapping(value = "/id")
    public TravelingPoint findById(@PathVariable long id)
            throws InvalidArgumentException, NonExistentItemException {
        return travelingPointService.findById(id);
    }

    @GetMapping(value = "/name")
    public TravelingPoint findByName(@PathVariable String name) throws InvalidArgumentException {
        return travelingPointService.findByName(name);
    }

    @GetMapping(value = "/all")
    public List<TravelingPoint> findAll() throws NonExistentItemException {
        return travelingPointService.findAll();
    }

    @PostMapping
    public void updateName(@PathVariable String newName, @PathVariable String oldName)
            throws InvalidArgumentException, NonExistentItemException {
        travelingPointService.updateName(newName, oldName);
    }

    @DeleteMapping(value = "/id")
    public void deleteById(@PathVariable long id) throws InvalidArgumentException {
        travelingPointService.deleteById(id);
    }
}