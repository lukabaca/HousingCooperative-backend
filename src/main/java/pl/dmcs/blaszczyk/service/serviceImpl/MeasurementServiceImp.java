package pl.dmcs.blaszczyk.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import pl.dmcs.blaszczyk.model.Entity.Measurement;
import pl.dmcs.blaszczyk.model.Exception.ResourceNotFoundException;
import pl.dmcs.blaszczyk.model.Response.EntityCreatedResponse;
import pl.dmcs.blaszczyk.repository.MeasurementRepository;
import pl.dmcs.blaszczyk.service.MeasurementService;

import java.util.List;

public class MeasurementServiceImp implements MeasurementService {

    @Autowired
    MeasurementRepository measurementRepository;

    @Override
    public Measurement getMeasurement(Long id) {
        return measurementRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());
    }

    @Override
    public List<Measurement> getMeasurements() {
        return measurementRepository.findAll();
    }

    @Override
    public EntityCreatedResponse createMeasurement() {
        return null;
    }

    @Override
    public EntityCreatedResponse changeMeasurementStatus(boolean isAccepted) {
        return null;
    }

    @Override
    public void assignMeasurementToBill(Long measurementId, Long billId) {

    }
}
