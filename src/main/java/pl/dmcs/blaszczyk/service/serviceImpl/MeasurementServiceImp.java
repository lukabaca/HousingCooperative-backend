package pl.dmcs.blaszczyk.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dmcs.blaszczyk.model.Entity.*;
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
import pl.dmcs.blaszczyk.service.PremiseService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
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

    @Autowired
    PremiseService premiseService;

    @Override
    public Measurement getMeasurement(Long id) {
        return measurementRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("measurement is not valid"));
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
            throw new WrongMeasurementDateException("wrong measurement date");
        }
        Premise premise = premiseService.getPremise(measurementRequest.getPremisesId());
        Measurement measurement = new Measurement();
        measurement.setColdWater(measurementRequest.getColdWater());
        measurement.setHotWater(measurementRequest.getHotWater());
        measurement.setElectricity(measurementRequest.getElectricity());
        measurement.setHeating(measurementRequest.getHeating());
        measurement.setYear(measurementRequest.getYear());
        measurement.setMonth(measurementRequest.getMonth());
        measurement.setChecked(false);
        measurement.setPremise(premise);
        Long measurementId = measurementRepository.saveAndFlush(measurement).getId();
        return new EntityCreatedResponse(measurementId);
    }

    private boolean checkIfMeasurementDateIsValid(int measuerementMonth, int measurementYear) {
        Date currentDate = new Date();
        LocalDate localDate = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int currentYear = localDate.getYear();
        int currentMonth = localDate.getMonthValue();
        if (measurementYear > currentYear) {
            return false;
        } else if (measurementYear == currentYear && measuerementMonth >= currentMonth) {
            return false;
        } else if (measuerementMonth < 1 || measuerementMonth > 12) {
            return false;
        } else {
            return true;
        }
    }


    @Override
    public EntityCreatedResponse updateMeasurement(Long id, MeasurementRequest measurementRequest) {
        if (measurementRequest == null) {
            throw new BadRequestException();
        }
        if (!canUpdateMeasurement(id)) {
            throw new BadRequestException("Measurement is already accepted, you can't update it");
        }
        if (!checkIfMeasurementDateIsValid(measurementRequest.getMonth(), measurementRequest.getYear())) {
            throw new WrongMeasurementDateException("wrong measurement date provided");
        }
        Measurement measurement = measurementRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Measurement not found"));
        measurement.setColdWater(measurementRequest.getColdWater());
        measurement.setHotWater(measurementRequest.getHotWater());
        measurement.setElectricity(measurementRequest.getElectricity());
        measurement.setHeating(measurementRequest.getHeating());
        measurement.setYear(measurementRequest.getYear());
        measurement.setMonth(measurementRequest.getMonth());
        measurement.setChecked(false);
        Long measurementId = measurementRepository.saveAndFlush(measurement).getId();
        return new EntityCreatedResponse(id);
    }

    private boolean canUpdateMeasurement(Long measurementId) {
        Measurement measurement = measurementRepository.findById(measurementId).orElseThrow(() -> new ResourceNotFoundException("measurement not found"));
        return !measurement.isAccepted();
    }

    @Override
    public EntityCreatedResponse changeMeasurementStatus(Long id, MeasurementStatusRequest measurementStatusRequest) {
        if (measurementStatusRequest == null) {
            throw new BadRequestException();
        }
        Measurement measurement = measurementRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("measurement not found"));
        measurement.setAccepted(measurementStatusRequest.isAccepted());
        measurement.setChecked(true);
        if (measurement.isAccepted()) {
            MeasurementCost measurementCost = measurementCostService.getMeasurementsCosts();
            Bill bill = billService.getCalculatedBill(measurement, measurementCost);
            billService.createBill(bill);
        }
        Long measurementId = measurementRepository.saveAndFlush(measurement).getId();
        return new EntityCreatedResponse(measurementId);
    }

    @Override
    public List<Measurement> getUserMeasurements(Long userId) {
        List<Measurement> measurements = measurementRepository.findAll();
        List<Measurement> userMeasurements = new ArrayList<>();
        for (Measurement measurement: measurements) {
            if (measurement != null) {
                Premise premise = measurement.getPremise();
                if (premise != null) {
                    for (AppUser appUser: premise.getAppUser()) {
                        if (userId.equals(appUser.getId())) {
                            userMeasurements.add(measurement);
                        }
                    }
                }
            }
        }
        return userMeasurements;
    }
}
