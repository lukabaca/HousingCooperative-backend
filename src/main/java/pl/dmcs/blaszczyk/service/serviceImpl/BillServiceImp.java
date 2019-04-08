package pl.dmcs.blaszczyk.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.dmcs.blaszczyk.model.Entity.Bill;
import pl.dmcs.blaszczyk.model.Exception.BadRequestException;
import pl.dmcs.blaszczyk.model.Request.BillStatusRequest;
import pl.dmcs.blaszczyk.model.Response.EntityCreatedResponse;
import pl.dmcs.blaszczyk.repository.BillRepository;
import pl.dmcs.blaszczyk.service.BillService;

import java.util.List;

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
}
