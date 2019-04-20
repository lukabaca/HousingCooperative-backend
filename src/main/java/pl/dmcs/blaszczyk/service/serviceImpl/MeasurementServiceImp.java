package pl.dmcs.blaszczyk.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dmcs.blaszczyk.model.Entity.Bill;
import pl.dmcs.blaszczyk.model.Entity.Measurement;
import pl.dmcs.blaszczyk.model.Entity.MeasurementCost;
import pl.dmcs.blaszczyk.model.Exception.BadRequestException;
import pl.dmcs.blaszczyk.model.Exception.ResourceNotFoundException;
import pl.dmcs.blaszczyk.model.Exception.WrongMeasurementDateException;
import pl.dmcs.blaszczyk.model.Request.MeasurementRequest;
import pl.dmcs.blaszczyk.model.Request.MeasurementStatusRequest;
import pl.dmcs.blaszczyk.model.Response.EntityCreatedResponse;
import pl.dmcs.blaszczyk.repository.MeasurementCostRepository;
import pl.dmcs.blaszczyk.repository.MeasurementRepository;
import pl.dmcs.blaszczyk.service.BillService;
import pl.dmcs.blaszczyk.service.MeasurementCostService;
import pl.dmcs.blaszczyk.service.MeasurementService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@Transactional
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
        if (!checkIfMeasurementDateIsValid(measurementRequest.getMonth(), measurementRequest.getYear())) {
            throw new WrongMeasurementDateException();
        }
        Measurement measurement = new Measurement();
        measurement.setColdWater(measurementRequest.getColdWater());
        measurement.setHotWater(measurementRequest.getHotWater());
        measurement.setElectricity(measurementRequest.getElectricity());
        measurement.setHeating(measurementRequest.getHeating());
        measurement.setYear(measurementRequest.getYear());
        measurement.setMonth(measurementRequest.getMonth());
        Long measurementId = measurementRepository.saveAndFlush(measurement).getId();
        MeasurementCost measurementCost = measurementCostService.getMeasurementsCosts();
        Bill bill = new Bill();
        bill = this.getCalculatedBill(measurement, measurementCost, bill);
        bill.setMeasurement(measurement);
        billService.createBill(bill);
        return new EntityCreatedResponse(measurementId);
    }

    private boolean checkIfMeasurementDateIsValid(int measuerementMonth, int measurementYear) {
        Date currentDate = new Date();
        LocalDate localDate = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int currentYear = localDate.getYear();
        int currentMonth = localDate.getMonthValue();
        if ((measurementYear < (currentYear - 1)) || (measurementYear > currentYear)) {
            return false;
        }
        if ((currentYear == measurementYear) && (measuerementMonth >= currentMonth)) {
            return false;
        }
        return true;
    }

    private Bill getCalculatedBill(Measurement measurement, MeasurementCost measurementCost, Bill bill) {
        if (measurement == null || measurementCost == null) {
            return null;
        }
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
        if (!canUpdateMeasurement(id)) {
            throw new BadRequestException();
        }
        if (!checkIfMeasurementDateIsValid(measurementRequest.getMonth(), measurementRequest.getYear())) {
            throw new WrongMeasurementDateException();
        }
        Measurement measurement = measurementRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());
        Bill bill = billService.findBillByMeasurementId(measurement.getId());
        measurement.setColdWater(measurementRequest.getColdWater());
        measurement.setHotWater(measurementRequest.getHotWater());
        measurement.setElectricity(measurementRequest.getElectricity());
        measurement.setHeating(measurementRequest.getHeating());
        measurement.setYear(measurementRequest.getYear());
        measurement.setMonth(measurementRequest.getMonth());
        Long measurementId = measurementRepository.saveAndFlush(measurement).getId();
        MeasurementCost measurementCost = measurementCostService.getMeasurementsCosts();
        bill = getCalculatedBill(measurement, measurementCost, bill);
        billService.createBill(bill);
        return new EntityCreatedResponse(id);
    }

    private boolean canUpdateMeasurement(Long measurementId) {
        Measurement measurement = measurementRepository.findById(measurementId).orElseThrow(() -> new ResourceNotFoundException());
        return !measurement.isAccepted();
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
