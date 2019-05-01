package pl.dmcs.blaszczyk.service;

import pl.dmcs.blaszczyk.model.Entity.Measurement;
import pl.dmcs.blaszczyk.model.Request.MeasurementRequest;
import pl.dmcs.blaszczyk.model.Request.MeasurementStatusRequest;
import pl.dmcs.blaszczyk.model.Response.EntityCreatedResponse;

import java.util.List;

public interface MeasurementService {
    Measurement getMeasurement(Long id);
    List<Measurement> getMeasurements();
    EntityCreatedResponse createMeasurement(MeasurementRequest measurementRequest);
    EntityCreatedResponse updateMeasurement(Long id, MeasurementRequest measurementRequest);
    EntityCreatedResponse changeMeasurementStatus(Long id, MeasurementStatusRequest measurementStatusRequest);
    List<Measurement> getUserMeasurements(Long userId);
}
