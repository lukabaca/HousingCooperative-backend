package pl.dmcs.blaszczyk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.dmcs.blaszczyk.model.Entity.Bill;
import pl.dmcs.blaszczyk.model.Request.BillStatusRequest;
import pl.dmcs.blaszczyk.model.Response.EntityCreatedResponse;
import pl.dmcs.blaszczyk.repository.BillRepository;
import pl.dmcs.blaszczyk.service.BillService;

import java.util.List;

@RestController
public class BillController {

    @Autowired
    private BillService billService;

    @GetMapping("bills")
    public ResponseEntity<List<Bill>> getBills() {
        List<Bill> bills = billService.getBills();
        return new ResponseEntity<List<Bill>>(bills, HttpStatus.OK);
    }

    @GetMapping("bill/{id}")
    public ResponseEntity<Bill> getBill(Long id) {
        Bill bill = billService.getBill(id);
        return new ResponseEntity<Bill>(bill, HttpStatus.OK);
    }

    @PutMapping("changeBillStatus/{id}")
    public ResponseEntity<EntityCreatedResponse> changeBillStatus(Long billId, BillStatusRequest billStatusRequest) {
        EntityCreatedResponse response = billService.changeBillPaymentStatus(billStatusRequest, billId);
        return new ResponseEntity<EntityCreatedResponse>(response, HttpStatus.CREATED);
    }

}
