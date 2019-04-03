package pl.dmcs.blaszczyk.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dmcs.blaszczyk.model.Entity.Building;
import pl.dmcs.blaszczyk.model.Entity.HousingCooperative;
import pl.dmcs.blaszczyk.model.Exception.BadRequestException;
import pl.dmcs.blaszczyk.model.Exception.ResourceNotFoundException;
import pl.dmcs.blaszczyk.model.Request.BuildingRequest;
import pl.dmcs.blaszczyk.model.Response.EntityCreatedResponse;
import pl.dmcs.blaszczyk.repository.BuildingRepository;
import pl.dmcs.blaszczyk.repository.HousingCooperativeRepository;
import pl.dmcs.blaszczyk.service.BuildingService;

import java.util.*;

@Service
public class BuildingServiceImp implements BuildingService {

    @Autowired
    BuildingRepository buildingRepository;

    @Autowired
    HousingCooperativeRepository housingCooperativeRepository;

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

    @Transactional
    @Override
    public void assignBuildingToHousingCooperative(Long buildingId, Long housingCooperativeId) {
        Optional<Building> optionalBuilding = buildingRepository.findById(buildingId);
        Optional<HousingCooperative> optionalHousingCooperative = housingCooperativeRepository.findById(housingCooperativeId);
        if (!optionalBuilding.isPresent() || !optionalHousingCooperative.isPresent()) {
            throw new ResourceNotFoundException();
        }
        HousingCooperative housingCooperative = optionalHousingCooperative.get();
        Building building = optionalBuilding.get();
        Set<Building> buildings = housingCooperative.getBuildings();
        building.setHousingCooperative(housingCooperative);
        buildings.add(building);
        housingCooperative.setBuildings(buildings);
        housingCooperativeRepository.save(housingCooperative);
    }
}
