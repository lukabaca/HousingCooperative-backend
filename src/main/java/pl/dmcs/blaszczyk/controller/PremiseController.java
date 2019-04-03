package pl.dmcs.blaszczyk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dmcs.blaszczyk.model.Entity.Premise;
import pl.dmcs.blaszczyk.model.Request.BuildingRequest;
import pl.dmcs.blaszczyk.model.Request.PremiseRequest;
import pl.dmcs.blaszczyk.model.Response.EntityCreatedResponse;
import pl.dmcs.blaszczyk.service.PremiseService;

import java.util.List;

@RestController
@RequestMapping("premise")
public class PremiseController {

    @Autowired
    PremiseService premiseService;

    @GetMapping("premises")
    public ResponseEntity<List<Premise>> getPremises() {
        List<Premise> premises = premiseService.getPremises();
        return new ResponseEntity<List<Premise>>(premises, HttpStatus.OK);
    }

    @PostMapping("premise")
    public ResponseEntity<EntityCreatedResponse> createPremise(@RequestBody PremiseRequest premiseRequest) {
        EntityCreatedResponse entityCreatedResponse = premiseService.createPremise(premiseRequest);
        return new ResponseEntity<EntityCreatedResponse>(entityCreatedResponse, HttpStatus.OK);
    }

    @GetMapping("premise/{id}")
    public ResponseEntity<Premise> createPremise(@RequestParam Long id) {
        Premise premise = premiseService.getPremise(id);
        return new ResponseEntity<Premise>(premise, HttpStatus.OK);
    }

    @PutMapping("premise/{id}")
    public ResponseEntity<EntityCreatedResponse> updatePremise(@RequestParam Long id, @RequestBody PremiseRequest premiseRequest) {
        EntityCreatedResponse entityCreatedResponse = premiseService.updatePremise(id, premiseRequest);
        return new ResponseEntity<EntityCreatedResponse>(entityCreatedResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("premise/{id}")
    public ResponseEntity<?> deletePremise(@RequestParam Long id) {
        premiseService.deletePremise(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
