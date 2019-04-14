package pl.dmcs.blaszczyk.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.dmcs.blaszczyk.model.Entity.Bill;
import pl.dmcs.blaszczyk.model.Exception.BadRequestException;
import pl.dmcs.blaszczyk.model.Exception.ResourceNotFoundException;
import pl.dmcs.blaszczyk.model.Request.BillStatusRequest;
import pl.dmcs.blaszczyk.model.Response.EntityCreatedResponse;
import pl.dmcs.blaszczyk.repository.BillRepository;
import pl.dmcs.blaszczyk.service.BillService;

import java.util.List;
import java.util.Optional;

@Service
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
    public EntityCreatedResponse changeBillPaymentStatus(BillStatusRequest billStatusRequest) {
        return null;
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
}
