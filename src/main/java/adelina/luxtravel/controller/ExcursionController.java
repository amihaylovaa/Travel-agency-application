package adelina.luxtravel.controller;

import adelina.luxtravel.domain.Excursion;
import adelina.luxtravel.domain.wrapper.Date;
import adelina.luxtravel.service.ExcursionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents controller for an excursion
 */
@RestController
@RequestMapping("/excursions")
public class ExcursionController {
    private final ExcursionService excursionService;

    @Autowired
    public ExcursionController(ExcursionService excursionService) {
        this.excursionService = excursionService;
    }

    @PostMapping
    public Excursion save(@RequestBody Excursion excursion) {
        return excursionService.save(excursion);
    }

    @GetMapping(value = "/{id}")
    public Excursion findById(@PathVariable("id") long id) {
        return excursionService.findById(id);
    }

    @GetMapping(value = "/dates")
    public List<Excursion> findByDates(@RequestParam(value = "from")  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startingDate,
                                       @RequestParam(value = "to")  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endingDate) {
        return excursionService.findByDates(startingDate, endingDate);
    }

    @GetMapping
    public List<Excursion> findAll() {
        return excursionService.findAll();
    }

    @PatchMapping(value = "{id}/dates")
    public Excursion updateDates(@PathVariable("id") long excursionId,
                                 @RequestBody Date dates) {
        return excursionService.updateDates(excursionId, dates);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteById(@PathVariable("id") long id) {
        excursionService.deleteById(id);
    }
}