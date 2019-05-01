package pl.dmcs.blaszczyk.service;

import pl.dmcs.blaszczyk.model.Entity.Premise;
import pl.dmcs.blaszczyk.model.Request.PremiseRequest;
import pl.dmcs.blaszczyk.model.Response.EntityCreatedResponse;

import java.util.List;

public interface PremiseService {
    List<Premise> getPremises();
    Premise getPremise(Long id);
    EntityCreatedResponse createPremise(PremiseRequest premiseRequest);
    EntityCreatedResponse updatePremise(Long id, PremiseRequest premiseRequest);
    void deletePremise(Long id);
    void addLocatorToPremises(Long premisesId, Long locatorId);
    void deleteLocatorFromPremises(Long premisesId, Long locatorId);
    Premise getUserPremise(Long userId);
}
