package pl.dmcs.blaszczyk.service;

import pl.dmcs.blaszczyk.model.Entity.Building;
import pl.dmcs.blaszczyk.model.Request.BuildingRequest;
import pl.dmcs.blaszczyk.model.Response.EntityCreatedResponse;

import java.util.List;

public interface BuildingService {
    List<Building> getBuildings();
    Building getBuilding(Long id);
    EntityCreatedResponse createBuilding(BuildingRequest buildingRequest);
    EntityCreatedResponse updateBuilding(Long id, BuildingRequest buildingRequest);
    void deleteBuilding(Long id);
    void assignBuildingToHousingCooperative(Long buildingId, Long housingCooperative);
}
