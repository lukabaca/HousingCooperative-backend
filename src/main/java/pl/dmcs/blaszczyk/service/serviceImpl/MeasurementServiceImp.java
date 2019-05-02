package pl.dmcs.blaszczyk.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dmcs.blaszczyk.model.Entity.*;
import pl.dmcs.blaszczyk.model.Exception.*;
import pl.dmcs.blaszczyk.model.Request.MeasurementRequest;
import pl.dmcs.blaszczyk.model.Request.MeasurementStatusRequest;
import pl.dmcs.blaszczyk.model.Response.EntityCreatedResponse;
import pl.dmcs.blaszczyk.repository.MeasurementCostRepository;
import pl.dmcs.blaszczyk.repository.MeasurementRepository;
import pl.dmcs.blaszczyk.service.BillService;
import pl.dmcs.blaszczyk.service.MeasurementCostService;
import pl.dmcs.blaszczyk.service.MeasurementService;
import pl.dmcs.blaszczyk.service.PremiseService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean hasUserRole = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_USER"));
        boolean isResourceForbidden = true;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof AppUser) {
            if (hasUserRole) {
                Measurement measurement = measurementRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("bill not found"));
                AppUser currentlyLoggedUser = (AppUser) principal;
                Premise premise = measurement.getPremise();
                for (AppUser appUser : premise.getAppUser()) {
                    if (appUser.getId().equals(currentlyLoggedUser.getId())) {
                        isResourceForbidden = false;
                        break;
                    }
                }
                if (isResourceForbidden) {
                    throw new ResourceForbiddenException("You can't watch other person bill");
                }
            }
        }
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
        if (isMeasurementAlreadySubmitted(measurementRequest)) {
            throw new MeasurementAlreadySubmittedException("Measurement for this period is already submitted");
        }
        Premise premise = premiseService.getPremise(measurementRequest.getPremisesId());
        Map<String, Double> averageMeasureElements = getAverageMeasurementElementsValue();
        Measurement measurement = new Measurement();
        measurement.setColdWater(measurementRequest.getColdWater() == null ? averageMeasureElements.get("COLD_WATER") : measurementRequest.getColdWater());
        measurement.setHotWater(measurementRequest.getHotWater() == null ? averageMeasureElements.get("HOT_WATER") : measurementRequest.getHotWater());
        measurement.setElectricity(measurementRequest.getElectricity() == null ? averageMeasureElements.get("ELECTRICITY") : measurementRequest.getElectricity());
        measurement.setHeating(measurementRequest.getHeating() == null ? averageMeasureElements.get("HEATING") : measurementRequest.getHeating());
        measurement.setYear(measurementRequest.getYear());
        measurement.setMonth(measurementRequest.getMonth());
        measurement.setChecked(false);
        measurement.setPremise(premise);
        Long measurementId = measurementRepository.saveAndFlush(measurement).getId();
        return new EntityCreatedResponse(measurementId);
    }

    private Map<String, Double> getAverageMeasurementElementsValue() {
        Map<String, Double> measureElements = new HashMap<String , Double>();
        Double coldWater = measurementRepository.getAverageValueOfColdWater() == null ? 0 : measurementRepository.getAverageValueOfColdWater();
        Double hotWater = measurementRepository.getAverageValueOfHotWater() == null ? 0 : measurementRepository.getAverageValueOfHotWater();
        Double electricity = measurementRepository.getAverageValueOfElectricity() == null ? 0 : measurementRepository.getAverageValueOfElectricity();
        Double heating = measurementRepository.getAverageValueOfHeating() == null ? 0 : measurementRepository.getAverageValueOfHeating();
        coldWater = (double) Math.round(coldWater);
        hotWater = (double) Math.round(hotWater);
        electricity = (double) Math.round(electricity);
        heating = (double) Math.round(heating);
        measureElements.put("COLD_WATER", coldWater);
        measureElements.put("HOT_WATER", hotWater);
        measureElements.put("ELECTRICITY", electricity);
        measureElements.put("HEATING", heating);
        return measureElements;
    }

    private boolean isMeasurementAlreadySubmitted(MeasurementRequest measurement) {
        if (measurement != null) {
            List<Measurement> measurements = measurementRepository.findAll();
            for (Measurement measurementTmp : measurements) {
                if (measurementTmp != null) {
                    int measurementTmpMonth = measurementTmp.getMonth();
                    int measurementTmpYear = measurementTmp.getYear();
                    if (measurementTmpMonth == measurement.getMonth() && measurementTmpYear == measurement.getYear()) {
                        return true;
                    }
                }
            }
            return false;
        }
        return true;
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
