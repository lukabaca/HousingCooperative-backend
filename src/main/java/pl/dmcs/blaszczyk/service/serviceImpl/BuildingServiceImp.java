package pl.dmcs.blaszczyk.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.dmcs.blaszczyk.model.Entity.Building;
import pl.dmcs.blaszczyk.model.Exception.BadRequestException;
import pl.dmcs.blaszczyk.model.Exception.ResourceNotFoundException;
import pl.dmcs.blaszczyk.model.Request.BuildingRequest;
import pl.dmcs.blaszczyk.model.Response.EntityCreatedResponse;
import pl.dmcs.blaszczyk.repository.BuildingRepository;
import pl.dmcs.blaszczyk.service.BuildingService;

import java.util.List;
import java.util.Optional;

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
        return buildingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());
    }

    @Override
    public EntityCreatedResponse createBuilding(BuildingRequest buildingRequest) {
        if (buildingRequest == null) {
            throw new BadRequestException();
        }
        Building building = new Building();
        building.setCity(buildingRequest.getCity());
        building.setNumber(buildingRequest.getNumber());
        building.setAddress(buildingRequest.getAddress());
        Long buildingId = buildingRepository.saveAndFlush(building).getId();
        return new EntityCreatedResponse(buildingId);
    }

    @Override
    public EntityCreatedResponse updateBuilding(Long id, BuildingRequest buildingRequest) {
        Optional<Building> optionalBuilding = buildingRepository.findById(id);
        if (!optionalBuilding.isPresent()) {
            throw new ResourceNotFoundException();
        }
        if (buildingRequest == null) {
            throw new BadRequestException();
        }
        Building building = optionalBuilding.get();
        building.setCity(buildingRequest.getCity());
        building.setNumber(buildingRequest.getNumber());
        building.setAddress(buildingRequest.getAddress());
        Long buildingId = buildingRepository.saveAndFlush(building).getId();
        return new EntityCreatedResponse(buildingId);
    }

    @Override
    public void deleteBuilding(Long id) {
        Optional<Building> optionalBuilding = buildingRepository.findById(id);
        if (!optionalBuilding.isPresent()) {
            throw new ResourceNotFoundException();
        }
        buildingRepository.delete(optionalBuilding.get());
    }

    @Override
    public void assignBuildingToHousingCooperative(Long buildingId, Long housingCooperative) {

    }
}
