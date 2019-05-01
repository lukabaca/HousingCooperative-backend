package pl.dmcs.blaszczyk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dmcs.blaszczyk.model.Entity.Bill;
import pl.dmcs.blaszczyk.model.Request.BillPaymentStatusRequest;
import pl.dmcs.blaszczyk.model.Request.BillStatusRequest;
import pl.dmcs.blaszczyk.model.Response.EntityCreatedResponse;
import pl.dmcs.blaszczyk.service.BillService;

import java.util.List;

@RestController
@RequestMapping("bill")
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

    @PutMapping("changeBillPaymentStatus/{id}")
    public ResponseEntity<EntityCreatedResponse> changeBillPaymentStatus(@PathVariable Long id, @RequestBody BillPaymentStatusRequest billPaymentStatusRequest) {
        EntityCreatedResponse response = billService.changeBillPaymentStatus(billPaymentStatusRequest, id);
        return new ResponseEntity<EntityCreatedResponse>(response, HttpStatus.CREATED);
    }

    @PutMapping("changeBillStatus/{id}")
    public ResponseEntity<EntityCreatedResponse> changeBillStatus(@PathVariable Long id, @RequestBody BillStatusRequest billStatusRequest) {
        EntityCreatedResponse response = billService.changeBillStatus(id, billStatusRequest);
        return new ResponseEntity<EntityCreatedResponse>(response, HttpStatus.CREATED);
    }
}
