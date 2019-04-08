package pl.dmcs.blaszczyk.service.serviceImpl;

import pl.dmcs.blaszczyk.model.Entity.Bill;
import pl.dmcs.blaszczyk.model.Response.EntityCreatedResponse;
import pl.dmcs.blaszczyk.service.BillService;

import java.util.List;

public class BillServiceImp implements BillService {
    @Override
    public Bill getBill(Long id) {
        return null;
    }

    @Override
    public List<Bill> getBills() {
        return null;
    }

    @Override
    public EntityCreatedResponse createBill() {
        return null;
    }

    @Override
    public EntityCreatedResponse changeBillPaymentStatus(boolean isPaid) {
        return null;
    }
}
