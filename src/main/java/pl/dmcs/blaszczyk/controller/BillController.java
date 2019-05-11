package pl.dmcs.blaszczyk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import pl.dmcs.blaszczyk.model.Entity.AppUser;
import pl.dmcs.blaszczyk.model.Entity.Bill;
import pl.dmcs.blaszczyk.model.Request.BillPaymentStatusRequest;
import pl.dmcs.blaszczyk.model.Request.BillRequest;
import pl.dmcs.blaszczyk.model.Request.BillStatusRequest;
import pl.dmcs.blaszczyk.model.Response.EntityCreatedResponse;
import pl.dmcs.blaszczyk.security.JWTTokenProvider;
import pl.dmcs.blaszczyk.service.BillService;
import pl.dmcs.blaszczyk.util.PdfGenerator;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("bill")
public class BillController {

    @Autowired
    private BillService billService;

    @Autowired
    JWTTokenProvider tokenProvider;

    @GetMapping("bills")
    public ResponseEntity<List<Bill>> getBills() {
        List<Bill> bills = billService.getBills();
        return new ResponseEntity<List<Bill>>(bills, HttpStatus.OK);
    }

    @GetMapping("bill/{id}")
    public ResponseEntity<Bill> getBill(@PathVariable Long id) {
        Bill bill = billService.getBill(id);
        return new ResponseEntity<Bill>(bill, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PutMapping("bill/{id}")
    public ResponseEntity<EntityCreatedResponse> updateBill(@PathVariable Long id, @RequestBody BillRequest billRequest) {
        EntityCreatedResponse response = billService.updateBill(billRequest, id);
        return new ResponseEntity<EntityCreatedResponse>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PutMapping("changeBillPaymentStatus/{id}")
    public ResponseEntity<EntityCreatedResponse> changeBillPaymentStatus(@PathVariable Long id, @RequestBody BillPaymentStatusRequest billPaymentStatusRequest) {
        EntityCreatedResponse response = billService.changeBillPaymentStatus(billPaymentStatusRequest, id);
        return new ResponseEntity<EntityCreatedResponse>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PutMapping("changeBillStatus/{id}")
    public ResponseEntity<EntityCreatedResponse> changeBillStatus(@PathVariable Long id, @RequestBody BillStatusRequest billStatusRequest) {
        EntityCreatedResponse response = billService.changeBillStatus(id, billStatusRequest);
        return new ResponseEntity<EntityCreatedResponse>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @GetMapping("getUserBills")
    public ResponseEntity<List<Bill>> getUserBills(HttpServletRequest request) {
        String token = tokenProvider.getJwtFromRequest(request);
        if (tokenProvider.validateToken(token)) {
            Long userId = tokenProvider.getUserIdFromJWT(token);
            List<Bill> userBills = billService.getUserBills(userId);
            return new ResponseEntity<List<Bill>>(userBills, HttpStatus.OK);
        }
        return new ResponseEntity<List<Bill>>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/billPdf/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> billPdf(@PathVariable Long id) throws IOException {
        Bill bill = billService.getBill(id);
        ByteArrayInputStream bis = PdfGenerator.billReport(bill);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=bill.pdf");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}
