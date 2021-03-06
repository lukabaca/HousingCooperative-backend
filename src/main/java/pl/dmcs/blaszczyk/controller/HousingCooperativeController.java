package pl.dmcs.blaszczyk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.dmcs.blaszczyk.model.Entity.Building;
import pl.dmcs.blaszczyk.model.Entity.HousingCooperative;
import pl.dmcs.blaszczyk.model.Request.BuildingRequest;
import pl.dmcs.blaszczyk.model.Response.EntityCreatedResponse;
import pl.dmcs.blaszczyk.service.BuildingService;
import pl.dmcs.blaszczyk.service.HousingCooperativeService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("housingCooperative")
public class HousingCooperativeController {
    @Autowired
    BuildingService buildingService;

    @Autowired
    HousingCooperativeService housingCooperativeService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping("buildings")
    public ResponseEntity<List<Building>> getBuildings() {
        List<Building> buildings = buildingService.getBuildings();
        return new ResponseEntity<List<Building>>(buildings, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("building")
    public ResponseEntity<EntityCreatedResponse> createBuilding(@Valid @RequestBody BuildingRequest buildingRequest) {
        EntityCreatedResponse entityCreatedResponse = buildingService.createBuilding(buildingRequest);
        return new ResponseEntity<EntityCreatedResponse>(entityCreatedResponse, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("building/{id}")
    public ResponseEntity<EntityCreatedResponse> updateBuilding(@RequestParam Long id, @RequestBody BuildingRequest buildingRequest) {
        EntityCreatedResponse entityCreatedResponse = buildingService.updateBuilding(id, buildingRequest);
        return new ResponseEntity<EntityCreatedResponse>(entityCreatedResponse, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping("building/{id}")
    public ResponseEntity<Building> getBuilding(@RequestParam Long id) {
        Building building = buildingService.getBuilding(id);
        return new ResponseEntity<Building>(building, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping("building/{id}")
    public ResponseEntity<?> deleteBuilding(@RequestParam Long id) {
        buildingService.deleteBuilding(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("housingCooperative/{id}")
    public ResponseEntity<HousingCooperative> getHousingCooperative(@RequestParam Long id) {
        HousingCooperative housingCooperative = housingCooperativeService.getHousingCooperative(id);
        return new ResponseEntity<HousingCooperative>(housingCooperative, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("assignBuildingToHousingCooperative/{buildingId}/{housingCooperativeId}")
    public ResponseEntity<?> assignBuildingToHousingCooperative(@RequestParam Long buildingId, @RequestParam Long housingCooperativeId) {
        buildingService.assignBuildingToHousingCooperative(buildingId, housingCooperativeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
