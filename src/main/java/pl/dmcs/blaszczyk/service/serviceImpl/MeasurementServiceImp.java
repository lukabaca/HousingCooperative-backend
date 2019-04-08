package pl.dmcs.blaszczyk.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.dmcs.blaszczyk.model.Entity.Bill;
import pl.dmcs.blaszczyk.model.Entity.Measurement;
import pl.dmcs.blaszczyk.model.Entity.MeasurementCost;
import pl.dmcs.blaszczyk.model.Exception.BadRequestException;
import pl.dmcs.blaszczyk.model.Exception.ResourceNotFoundException;
import pl.dmcs.blaszczyk.model.Request.MeasurementRequest;
import pl.dmcs.blaszczyk.model.Request.MeasurementStatusRequest;
import pl.dmcs.blaszczyk.model.Response.EntityCreatedResponse;
import pl.dmcs.blaszczyk.repository.MeasurementCostRepository;
import pl.dmcs.blaszczyk.repository.MeasurementRepository;
import pl.dmcs.blaszczyk.service.BillService;
import pl.dmcs.blaszczyk.service.MeasurementCostService;
import pl.dmcs.blaszczyk.service.MeasurementService;

import java.util.List;

@Service
public class MeasurementServiceImp implements MeasurementService {

    @Autowired
    MeasurementRepository measurementRepository;

    @Autowired
    MeasurementCostService measurementCostService;

    @Autowired
    BillService billService;

    @Override
    public Measurement getMeasurement(Long id) {
        return measurementRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());
    }

    @Override
    public List<Measurement> getMeasurements() {
        return measurementRepository.findAll();
    }

    @Override
    public EntityCreatedResponse createMeasurement(MeasurementRequest measurementRequest) {
        if (measurementRequest == null) {
            throw new BadRequestException();
        }
        Measurement measurement = new Measurement();
        measurement.setColdWater(measurementRequest.getColdWater());
        measurement.setHotWater(measurementRequest.getHotWater());
        measurement.setElectricity(measurementRequest.getElectricity());
        measurement.setHeating(measurementRequest.getHeating());
        Long measurementId = measurementRepository.saveAndFlush(measurement).getId();
        MeasurementCost measurementCost = measurementCostService.getMeasurementsCosts();
        Bill bill = this.getCalculatedBill(measurement, measurementCost);
        bill.setMeasurement(measurement);
        billService.createBill(bill);
        return new EntityCreatedResponse(measurementId);
    }

    private Bill getCalculatedBill(Measurement measurement, MeasurementCost measurementCost) {
        if (measurement == null || measurementCost == null) {
            return null;
        }
        Bill bill = new Bill();
        bill.setColdWaterCost(measurement.getColdWater() * measurementCost.getColdWaterCost());
        bill.setHotWaterCost(measurement.getHotWater() * measurementCost.getHotWaterCost());
        bill.setElectricityCost(measurement.getElectricity() * measurementCost.getElectricityCost());
        bill.setHeatingCost(measurement.getHeating() * measurementCost.getHeatingCost());
        return bill;
    }

    @Override
    public EntityCreatedResponse updateMeasurement(Long id, MeasurementRequest measurementRequest) {
        if (measurementRequest == null) {
            throw new BadRequestException();
        }
        Measurement measurement = measurementRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());
        measurement.setColdWater(measurementRequest.getColdWater());
        measurement.setHotWater(measurement.getHotWater());
        measurement.setElectricity(measurement.getElectricity());
        measurement.setHeating(measurement.getHeating());
        Long measurementId = measurementRepository.saveAndFlush(measurement).getId();
        return new EntityCreatedResponse(measurementId);
    }

    @Override
    public EntityCreatedResponse changeMeasurementStatus(Long id, MeasurementStatusRequest measurementStatusRequest) {
        if (measurementStatusRequest == null) {
            throw new BadRequestException();
        }
        Measurement measurement = measurementRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());
        measurement.setAccepted(measurementStatusRequest.isAccepted());
        Long measurementId = measurementRepository.saveAndFlush(measurement).getId();
        return new EntityCreatedResponse(measurementId);
    }
}
