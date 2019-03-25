package pl.dmcs.blaszczyk.service.serviceIMPL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.dmcs.blaszczyk.model.Entity.Building;
import pl.dmcs.blaszczyk.model.Request.BuildingRequest;
import pl.dmcs.blaszczyk.model.Response.EntityCreatedResponse;
import pl.dmcs.blaszczyk.repository.BuildingRepository;
import pl.dmcs.blaszczyk.service.BuildingService;

import java.util.List;

@Service
public class BuildingServiceImp implements BuildingService {

    @Autowired
    BuildingRepository buildingRepository;

    @Override
    public List<Building> getBuildings() {
        return buildingRepository.findAll();
    }

    @Override
    public Building getBuilding(Long id) {
        return null;
    }

    @Override
    public EntityCreatedResponse createBuilding(BuildingRequest buildingRequest) {
        Building building = new Building();
        building.setCity(buildingRequest.getCity());
        building.setNumber(buildingRequest.getNumber());
        building.setAddress(buildingRequest.getAddress());
        Long buildingId = buildingRepository.saveAndFlush(building).getId();
        return new EntityCreatedResponse(buildingId);
    }

    @Override
    public EntityCreatedResponse updateBuilding(Long id, BuildingRequest buildingRequest) {
        return null;
    }

    @Override
    public void assignBuildingToHousingCooperative(Long buildingId, Long housingCooperative) {

    }
}
