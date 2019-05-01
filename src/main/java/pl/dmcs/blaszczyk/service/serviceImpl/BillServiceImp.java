package pl.dmcs.blaszczyk.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dmcs.blaszczyk.model.Entity.Bill;
import pl.dmcs.blaszczyk.model.Entity.Measurement;
import pl.dmcs.blaszczyk.model.Entity.MeasurementCost;
import pl.dmcs.blaszczyk.model.Exception.BadRequestException;
import pl.dmcs.blaszczyk.model.Exception.ResourceNotFoundException;
import pl.dmcs.blaszczyk.model.Request.BillStatusRequest;
import pl.dmcs.blaszczyk.model.Response.EntityCreatedResponse;
import pl.dmcs.blaszczyk.repository.BillRepository;
import pl.dmcs.blaszczyk.service.BillService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BillServiceImp implements BillService {

    @Autowired
    BillRepository billRepository;

    @Override
    public Bill getBill(Long id) {
        return null;
    }

    @Override
    public List<Bill> getBills() {
        return null;
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
    public EntityCreatedResponse changeBillPaymentStatus(BillStatusRequest billStatusRequest, Long id) {
        if (billStatusRequest == null) {
            throw new BadRequestException();
        }
        Bill bill = billRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("bill not found"));
        bill.setPaid(billStatusRequest.isPaid());
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
        return bill;
    }
}
