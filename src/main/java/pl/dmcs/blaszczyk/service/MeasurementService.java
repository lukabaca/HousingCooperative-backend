package pl.dmcs.blaszczyk.service;

import pl.dmcs.blaszczyk.model.Entity.Measurement;
import pl.dmcs.blaszczyk.model.Response.EntityCreatedResponse;

import java.util.List;

public interface MeasurementService {
    Measurement getMeasurement(Long id);
    List<Measurement> getMeasurements();
    EntityCreatedResponse createMeasurement();
    EntityCreatedResponse changeMeasurementStatus(boolean isAccepted);
    void assignMeasurementToBill(Long measurementId, Long billId);
}
