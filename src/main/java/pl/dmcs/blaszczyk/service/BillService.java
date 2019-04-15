package pl.dmcs.blaszczyk.service;

import pl.dmcs.blaszczyk.model.Entity.Bill;
import pl.dmcs.blaszczyk.model.Request.BillStatusRequest;
import pl.dmcs.blaszczyk.model.Response.EntityCreatedResponse;

import java.util.List;

public interface BillService {
    Bill getBill(Long id);
    List<Bill> getBills();
    EntityCreatedResponse createBill(Bill bill);
    EntityCreatedResponse changeBillPaymentStatus(BillStatusRequest billStatusRequest);
    Bill findBillByMeasurementId(Long measurementId);
}
