package pl.dmcs.blaszczyk.controller;
import pl.dmcs.blaszczyk.model.Entity.Resident;
import pl.dmcs.blaszczyk.service.ResidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class TestController {

    @Autowired
    private ResidentService residentService;

    @PostMapping(value="/test")
    public Long createResident(@RequestBody Resident resident) {
        return residentService.createResident(resident);
    }

    @GetMapping(value="/test")
    public List<Resident> test() {
        return residentService.getResidents();
    }

    @GetMapping(value="/test/{id}")
    public ResponseEntity<Resident> findResident(@PathVariable("id") Long id) {
        return residentService.getResident(id);
    }
}

