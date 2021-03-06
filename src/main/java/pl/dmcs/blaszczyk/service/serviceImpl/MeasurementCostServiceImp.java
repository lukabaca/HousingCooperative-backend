package pl.dmcs.blaszczyk.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dmcs.blaszczyk.model.Entity.MeasurementCost;
import pl.dmcs.blaszczyk.model.Exception.ResourceNotFoundException;
import pl.dmcs.blaszczyk.repository.MeasurementCostRepository;
import pl.dmcs.blaszczyk.service.MeasurementCostService;

@Service
@Transactional
public class MeasurementCostServiceImp implements MeasurementCostService {

    @Autowired
    MeasurementCostRepository measurementCostRepository;

    @Override
    public MeasurementCost getMeasurementsCosts() {
        return measurementCostRepository.findById(Long.valueOf(1)).orElseThrow(() -> new ResourceNotFoundException("measurements costs not found"));
    }
}
