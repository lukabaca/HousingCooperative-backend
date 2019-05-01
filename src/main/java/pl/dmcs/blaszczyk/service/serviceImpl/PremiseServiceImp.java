package pl.dmcs.blaszczyk.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dmcs.blaszczyk.model.Entity.AppUser;
import pl.dmcs.blaszczyk.model.Entity.Building;
import pl.dmcs.blaszczyk.model.Entity.Premise;
import pl.dmcs.blaszczyk.model.Exception.BadRequestException;
import pl.dmcs.blaszczyk.model.Exception.ResourceNotFoundException;
import pl.dmcs.blaszczyk.model.Request.PremiseRequest;
import pl.dmcs.blaszczyk.model.Response.EntityCreatedResponse;
import pl.dmcs.blaszczyk.repository.AppUserRepository;
import pl.dmcs.blaszczyk.repository.BuildingRepository;
import pl.dmcs.blaszczyk.repository.PremiseRepository;
import pl.dmcs.blaszczyk.service.PremiseService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class PremiseServiceImp implements PremiseService {

    @Autowired
    PremiseRepository premiseRepository;

    @Autowired
    BuildingRepository buildingRepository;

    @Autowired
    AppUserRepository appUserRepository;

    @Override
    public List<Premise> getPremises() {
        return premiseRepository.findAll();
    }

    @Override
    public Premise getPremise(Long id) {
        return premiseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("premise not found"));
    }

    @Override
    public EntityCreatedResponse createPremise(PremiseRequest premiseRequest) {
        if (premiseRequest == null) {
            throw new BadRequestException();
        }
        Premise premise = new Premise();
        premise.setNumber(premiseRequest.getNumber());
        premise.setRoomCount(premiseRequest.getRoomCount());
        premise.setSpace(premiseRequest.getRoomCount());
        Building building = buildingRepository.findById(premiseRequest.getBuildingId()).orElseThrow(() -> new ResourceNotFoundException("building not found"));
        premise.setBuilding(building);
        Long premiseId = premiseRepository.saveAndFlush(premise).getId();
        return new EntityCreatedResponse(premiseId);
    }

    @Override
    public EntityCreatedResponse updatePremise(Long id, PremiseRequest premiseRequest) {
        if (premiseRequest == null) {
            throw new BadRequestException();
        }
        Premise premise = premiseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("premise not found"));
        premise.setNumber(premiseRequest.getNumber());
        premise.setRoomCount(premiseRequest.getRoomCount());
        premise.setSpace(premiseRequest.getRoomCount());
        Long premiseId = premiseRepository.saveAndFlush(premise).getId();
        return new EntityCreatedResponse(premiseId);
    }

    @Override
    public void deletePremise(Long id) {
        Premise premise = premiseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("premise not found"));
        Building building = premise.getBuilding();
        Set<Premise> premises = new HashSet<>(building.getPremises());
        for (Premise premiseTmp: premises) {
            if (premiseTmp.getId() == premise.getId()) {
                building.getPremises().remove(premiseTmp);
            }
        }
        premiseRepository.delete(premise);
    }

    @Override
    public void addLocatorToPremises(Long premisesId, Long locatorId) {
        Premise premise = premiseRepository.findById(premisesId).orElseThrow(() -> new ResourceNotFoundException("Premise not found"));
        AppUser appUser = appUserRepository.findById(locatorId).orElseThrow(() -> new ResourceNotFoundException("Locator not found"));
        premise.getAppUser().add(appUser);
        premiseRepository.saveAndFlush(premise);
    }

    @Override
    public void deleteLocatorFromPremises(Long premisesId, Long locatorId) {
        Premise premise = premiseRepository.findById(premisesId).orElseThrow(() -> new ResourceNotFoundException("Premise not found"));
        AppUser appUser = appUserRepository.findById(locatorId).orElseThrow(() -> new ResourceNotFoundException("Locator not found"));
        if (!premise.getAppUser().remove(appUser)) {
            throw new BadRequestException("This locator doesn't belong to this premise");
        }
        premiseRepository.saveAndFlush(premise);
    }

    @Override
    public Premise getUserPremise(Long userId) {
        List<Premise> premises = premiseRepository.findAll();
        for (Premise premise : premises) {
            if (premise != null) {
                Set<AppUser> appUsers = premise.getAppUser();
                for (AppUser appUser : appUsers) {
                    if (appUser != null) {
                        if (appUser.getId().equals(userId)) {
                            return premise;
                        }
                    }
                }
            }
        }
        throw new ResourceNotFoundException("Premise for user not found");
    }
}
