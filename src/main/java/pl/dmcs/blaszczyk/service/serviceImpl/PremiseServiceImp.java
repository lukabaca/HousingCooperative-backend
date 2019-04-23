package pl.dmcs.blaszczyk.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dmcs.blaszczyk.model.Entity.Building;
import pl.dmcs.blaszczyk.model.Entity.Premise;
import pl.dmcs.blaszczyk.model.Exception.BadRequestException;
import pl.dmcs.blaszczyk.model.Exception.ResourceNotFoundException;
import pl.dmcs.blaszczyk.model.Request.PremiseRequest;
import pl.dmcs.blaszczyk.model.Response.EntityCreatedResponse;
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
        Optional<Premise> optionalPremise = premiseRepository.findById(id);
        if (!optionalPremise.isPresent()) {
            throw new ResourceNotFoundException();
        }
        if (premiseRequest == null) {
            throw new BadRequestException();
        }
        Premise premise = optionalPremise.get();
        premise.setNumber(premiseRequest.getNumber());
        premise.setRoomCount(premiseRequest.getRoomCount());
        premise.setSpace(premiseRequest.getRoomCount());
        Long premiseId = premiseRepository.saveAndFlush(premise).getId();
        return new EntityCreatedResponse(premiseId);
    }

    @Override
    public void deletePremise(Long id) {
        Premise premise = premiseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());
        Building building = premise.getBuilding();
        Set<Premise> premises = new HashSet<>(building.getPremises());
        for (Premise premiseTmp: premises) {
            if (premiseTmp.getId() == premise.getId()) {
                building.getPremises().remove(premiseTmp);
            }
        }
        premiseRepository.delete(premise);
    }
}
