package pl.dmcs.blaszczyk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.dmcs.blaszczyk.model.Entity.Measurement;
import pl.dmcs.blaszczyk.model.Request.MeasurementRequest;
import pl.dmcs.blaszczyk.model.Response.EntityCreatedResponse;
import pl.dmcs.blaszczyk.service.MeasurementService;

import java.util.List;

@RestController("measurement")
public class MeasurementController {

    @Autowired
    MeasurementService measurementService;

    @GetMapping("measurements")
    public ResponseEntity<List<Measurement>> getMeasurements() {
        List<Measurement> measurementsList = measurementService.getMeasurements();
        return new ResponseEntity<List<Measurement>>(measurementsList, HttpStatus.OK);
    }

    @PostMapping("measurement")
    public ResponseEntity<EntityCreatedResponse> createMeasurement(@RequestBody MeasurementRequest measurementRequest) {
        EntityCreatedResponse entityCreatedResponse = measurementService.createMeasurement(measurementRequest);
        return new ResponseEntity<EntityCreatedResponse>(entityCreatedResponse, HttpStatus.CREATED);
    }

}
