package pl.dmcs.blaszczyk.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dmcs.blaszczyk.model.Entity.AppUser;
import pl.dmcs.blaszczyk.model.Entity.Building;
import pl.dmcs.blaszczyk.model.Entity.HousingCooperative;
import pl.dmcs.blaszczyk.model.Exception.BadRequestException;
import pl.dmcs.blaszczyk.model.Exception.ResourceNotFoundException;
import pl.dmcs.blaszczyk.model.Request.BuildingRequest;
import pl.dmcs.blaszczyk.model.Response.EntityCreatedResponse;
import pl.dmcs.blaszczyk.repository.AppUserRepository;
import pl.dmcs.blaszczyk.repository.BuildingRepository;
import pl.dmcs.blaszczyk.repository.HousingCooperativeRepository;
import pl.dmcs.blaszczyk.service.BuildingService;

import java.util.*;

@Service
@Transactional
public class BuildingServiceImp implements BuildingService {

    @Autowired
    BuildingRepository buildingRepository;

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    HousingCooperativeRepository housingCooperativeRepository;

    @Override
    public List<Building> getBuildings() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean hasManagerRole = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_MANAGER"));
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (hasManagerRole) {
            if (principal instanceof AppUser) {
                AppUser currentlyLoggedUser = (AppUser) principal;
                List<Building> buildings = buildingRepository.findAll();
                List<Building> managerBuildings = new ArrayList<>();
                for (Building building : buildings) {
                    if (building != null) {
                        AppUser manager = building.getManager();
                        if (manager != null) {
                            if (manager.getId().equals(currentlyLoggedUser.getId())) {
                                managerBuildings.add(building);
                            }
                        }
                    }
                }
                return managerBuildings;
            }
        }
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
        Long housingCooperativeId = Long.valueOf(1);
        Building building = new Building();
        building.setCity(buildingRequest.getCity());
        building.setNumber(buildingRequest.getNumber());
        building.setAddress(buildingRequest.getAddress());
        Long managerId = buildingRequest.getManagerId();
        if (managerId != null) {
            AppUser manager = appUserRepository.findById(managerId).orElse(null);
            building.setManager(manager);
        }
        HousingCooperative housingCooperative = this.housingCooperativeRepository.findById(housingCooperativeId).orElseThrow(() -> new ResourceNotFoundException());
        building.setHousingCooperative(housingCooperative);
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
