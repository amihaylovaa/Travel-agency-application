package adelina.luxtravel.controller;

import adelina.luxtravel.domain.TravelingPoint;
import adelina.luxtravel.exception.InvalidArgumentException;
import adelina.luxtravel.exception.NonExistentItemException;
import adelina.luxtravel.service.TravelingPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
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
    public TravelingPoint save(@RequestBody TravelingPoint travelingPoint) {
        return travelingPointService.save(travelingPoint);
    }

    @PostMapping(value = "/all")
    public List<TravelingPoint> saveAll(@RequestBody List<TravelingPoint> travelingPoints) {
        return travelingPointService.saveAll(travelingPoints);
    }

    @GetMapping(value = "id/{id}")
    public TravelingPoint findById(@PathVariable("id") long id) {
        return travelingPointService.findById(id);
    }

    @GetMapping(value = "name/{name}")
    public TravelingPoint findByName(@PathVariable("name") String name) {
        return travelingPointService.findByName(name);
    }

    @GetMapping(value = "/all")
    public List<TravelingPoint> findAll() {
        return travelingPointService.findAll();
    }

    @PutMapping(value = "/{newName}/{oldName}")
    public void updateName(@PathVariable("newName") String newName,
                           @PathVariable("oldName") String oldName) {
        travelingPointService.updateName(newName, oldName);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteById(@PathVariable long id) {
        travelingPointService.deleteById(id);
    }
}