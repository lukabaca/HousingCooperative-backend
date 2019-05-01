package pl.dmcs.blaszczyk.service;

import pl.dmcs.blaszczyk.model.Entity.Bill;
import pl.dmcs.blaszczyk.model.Entity.Measurement;
import pl.dmcs.blaszczyk.model.Entity.MeasurementCost;
import pl.dmcs.blaszczyk.model.Request.BillRequest;
import pl.dmcs.blaszczyk.model.Request.BillPaymentStatusRequest;
import pl.dmcs.blaszczyk.model.Request.BillStatusRequest;
import pl.dmcs.blaszczyk.model.Response.EntityCreatedResponse;

import java.util.List;

public interface BillService {
    Bill getBill(Long id);
    List<Bill> getBills();
    EntityCreatedResponse createBill(Bill bill);
    EntityCreatedResponse changeBillPaymentStatus(BillPaymentStatusRequest billPaymentStatusRequest, Long id);
    Bill findBillByMeasurementId(Long measurementId);
    Bill getCalculatedBill(Measurement measurement, MeasurementCost measurementCost);
    EntityCreatedResponse updateBill(BillRequest billRequest, Long id);
    EntityCreatedResponse changeBillStatus(Long id, BillStatusRequest billStatusRequest);
    List<Bill> getUserBills(Long userId);
}
