package pl.dmcs.blaszczyk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dmcs.blaszczyk.model.Entity.Building;
import pl.dmcs.blaszczyk.model.Request.BuildingRequest;
import pl.dmcs.blaszczyk.model.Response.EntityCreatedResponse;
import pl.dmcs.blaszczyk.service.BuildingService;

import java.util.List;

@RestController
@RequestMapping("housingCooperative")
public class HousingCooperativeController {
    @Autowired
    BuildingService buildingService;

    @GetMapping("buildings")
    public ResponseEntity<List<Building>> getBuildings() {
        List<Building> buildings = buildingService.getBuildings();
        return new ResponseEntity<List<Building>>(buildings, HttpStatus.OK);
    }

    @PostMapping("building")
    public ResponseEntity<EntityCreatedResponse> createBuilding(@RequestBody BuildingRequest buildingRequest) {
        EntityCreatedResponse entityCreatedResponse = buildingService.createBuilding(buildingRequest);
        if (entityCreatedResponse != null) {
            return new ResponseEntity<EntityCreatedResponse>(entityCreatedResponse, HttpStatus.CREATED);
        }
        return new ResponseEntity<EntityCreatedResponse>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("building/{id}")
    public ResponseEntity<Building> getBuilding(@RequestParam Long id) {
        Building building = buildingService.getBuilding(id);
        return new ResponseEntity<Building>(building, HttpStatus.OK);
    }
}
