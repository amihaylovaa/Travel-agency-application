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
    public TravelingPoint save(@RequestBody TravelingPoint travelingPoint)
            throws InvalidArgumentException {
        return travelingPointService.save(travelingPoint);
    }

    @PostMapping(value = "/list")
    public List<TravelingPoint> saveAll(@RequestBody List<TravelingPoint> travelingPoints)
            throws InvalidArgumentException, NonExistentItemException {
        return travelingPointService.saveAll(travelingPoints);
    }

    @GetMapping(value = "/{id}")
    public TravelingPoint findById(@PathParam("id") long id)
            throws InvalidArgumentException, NonExistentItemException {
        return travelingPointService.findById(id);
    }

    @GetMapping(value = "/{name}")
    public TravelingPoint findByName(@PathParam("name") String name) throws InvalidArgumentException {
        return travelingPointService.findByName(name);
    }

    @GetMapping(value = "/all")
    public List<TravelingPoint> findAll() throws NonExistentItemException {
        return travelingPointService.findAll();
    }

    @PutMapping(value = "/{newName}/{oldName}")
    public void updateName(@PathParam("newName") String newName, @PathParam("oldName") String oldName)
            throws InvalidArgumentException {
        travelingPointService.updateName(newName, oldName);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteById(@PathVariable long id) throws InvalidArgumentException {
        travelingPointService.deleteById(id);
    }
}