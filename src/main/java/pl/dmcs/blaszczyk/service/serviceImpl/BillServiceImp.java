package pl.dmcs.blaszczyk.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dmcs.blaszczyk.model.Entity.*;
import pl.dmcs.blaszczyk.model.Exception.BadRequestException;
import pl.dmcs.blaszczyk.model.Exception.BillAlreadyPaidException;
import pl.dmcs.blaszczyk.model.Exception.ResourceForbiddenException;
import pl.dmcs.blaszczyk.model.Exception.ResourceNotFoundException;
import pl.dmcs.blaszczyk.model.Request.BillRequest;
import pl.dmcs.blaszczyk.model.Request.BillPaymentStatusRequest;
import pl.dmcs.blaszczyk.model.Request.BillStatusRequest;
import pl.dmcs.blaszczyk.model.Response.EntityCreatedResponse;
import pl.dmcs.blaszczyk.repository.BillRepository;
import pl.dmcs.blaszczyk.service.BillService;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class BillServiceImp implements BillService {

    @Autowired
    BillRepository billRepository;

    @Override
    public Bill getBill(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean hasUserRole = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_USER"));
        boolean isResourceForbidden = true;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof AppUser) {
            if (hasUserRole) {
                Bill bill = billRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("bill not found"));
                AppUser currentlyLoggedUser = (AppUser) principal;
                Measurement measurement = bill.getMeasurement();
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
        return billRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("bill not found"));
    }

    @Override
    public List<Bill> getBills() {
        return billRepository.findAll();
    }

    @Override
    public EntityCreatedResponse createBill(Bill bill) {
        if (bill == null) {
            throw new BadRequestException();
        }
        Long billId = billRepository.saveAndFlush(bill).getId();
        return new EntityCreatedResponse(billId);
    }

    @Override
    public EntityCreatedResponse changeBillPaymentStatus(BillPaymentStatusRequest billPaymentStatusRequest, Long id) {
        if (billPaymentStatusRequest == null) {
            throw new BadRequestException();
        }
        Bill bill = billRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("bill not found"));
        if (!bill.isAccepted()) {
            throw new BadRequestException("Cant change payment status of not accepted bill");
        }
        if (bill.isPaid()) {
            throw new BillAlreadyPaidException("Bill is already paid");
        }
        bill.setPaid(billPaymentStatusRequest.isPaid());
        Long billId = billRepository.saveAndFlush(bill).getId();
        return new EntityCreatedResponse(billId);
    }

    @Override
    public Bill findBillByMeasurementId(Long measurementId) {
       List<Bill> bills = billRepository.findAll();
       for (Bill bill : bills) {
            if (bill != null && (bill.getMeasurement().getId() == measurementId)) {
                return bill;
           }
       }
       throw new ResourceNotFoundException();
    }

    @Override
    public Bill getCalculatedBill(Measurement measurement, MeasurementCost measurementCost) {
        if (measurement == null || measurementCost == null) {
            return null;
        }
        Bill bill = new Bill();
        bill.setMeasurement(measurement);
        bill.setColdWaterCost(measurement.getColdWater() * measurementCost.getColdWaterCost());
        bill.setHotWaterCost(measurement.getHotWater() * measurementCost.getHotWaterCost());
        bill.setElectricityCost(measurement.getElectricity() * measurementCost.getElectricityCost());
        bill.setHeatingCost(measurement.getHeating() * measurementCost.getHeatingCost());
        bill.setChecked(false);
        return bill;
    }

    @Override
    public EntityCreatedResponse updateBill(BillRequest billRequest, Long id) {
        if (billRequest == null) {
            throw new BadRequestException();
        }
        Bill bill = billRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Bill not found"));
        bill.setHeatingCost(billRequest.getHeatingCost());
        bill.setElectricityCost(billRequest.getElectricityCost());
        bill.setColdWaterCost(billRequest.getColdWaterCost());
        bill.setHotWaterCost(billRequest.getHotWaterCost());
        bill.setChecked(false);
        Long billId = billRepository.saveAndFlush(bill).getId();
        return new EntityCreatedResponse(billId);
    }

    @Override
    public EntityCreatedResponse changeBillStatus(Long id, BillStatusRequest billStatusRequest) {
        if (billStatusRequest == null) {
            throw new BadRequestException();
        }
        Bill bill = billRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Bill not found"));
        bill.setAccepted(billStatusRequest.isAccepted());
        bill.setChecked(true);
        Long billId = billRepository.saveAndFlush(bill).getId();
        return new EntityCreatedResponse(billId);
    }

    @Override
    public List<Bill> getUserBills(Long userId) {
        List<Bill> bills = billRepository.findAll();
        List<Bill> userBills = new ArrayList<>();
        for (Bill bill: bills) {
            if (bill != null) {
                Measurement measurement = bill.getMeasurement();
                if (measurement != null) {
                    Premise premise = measurement.getPremise();
                    if (premise != null) {
                        for (AppUser appUser: premise.getAppUser()) {
                            if (userId.equals(appUser.getId())) {
                                userBills.add(bill);
                            }
                        }
                    }
                }
            }
        }
        return userBills;
    }
}
