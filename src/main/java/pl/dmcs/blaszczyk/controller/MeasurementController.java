package pl.dmcs.blaszczyk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dmcs.blaszczyk.model.Entity.Measurement;
import pl.dmcs.blaszczyk.model.Request.MeasurementRequest;
import pl.dmcs.blaszczyk.model.Request.MeasurementStatusRequest;
import pl.dmcs.blaszczyk.model.Response.EntityCreatedResponse;
import pl.dmcs.blaszczyk.service.MeasurementService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("measurement")
public class MeasurementController {

    @Autowired
    MeasurementService measurementService;

    @GetMapping("measurements")
    public ResponseEntity<List<Measurement>> getMeasurements() {
        List<Measurement> measurementsList = measurementService.getMeasurements();
        return new ResponseEntity<List<Measurement>>(measurementsList, HttpStatus.OK);
    }

    @GetMapping("measurement/{id}")
    public ResponseEntity<Measurement> getMeasurement(@PathVariable Long id) {
        Measurement measurement = measurementService.getMeasurement(id);
        return new ResponseEntity<Measurement>(measurement, HttpStatus.OK);
    }

    @PostMapping("measurement")
    public ResponseEntity<EntityCreatedResponse> createMeasurement(@Valid @RequestBody MeasurementRequest measurementRequest) {
        EntityCreatedResponse entityCreatedResponse = measurementService.createMeasurement(measurementRequest);
        return new ResponseEntity<EntityCreatedResponse>(entityCreatedResponse, HttpStatus.CREATED);
    }

    @PutMapping("measurement/{id}")
    public ResponseEntity<EntityCreatedResponse> updateMeasurement(@PathVariable Long id, @RequestBody MeasurementRequest measurementRequest) {
        EntityCreatedResponse entityCreatedResponse = measurementService.updateMeasurement(id, measurementRequest);
        return new ResponseEntity<EntityCreatedResponse>(entityCreatedResponse, HttpStatus.CREATED);
    }

    @PutMapping("changeMeasurementStatus/{id}")
    public ResponseEntity<EntityCreatedResponse> changeMeasurementStatus(@PathVariable Long id, @RequestBody MeasurementStatusRequest measurementStatusRequest) {
        EntityCreatedResponse entityCreatedResponse = measurementService.changeMeasurementStatus(id, measurementStatusRequest);
        return new ResponseEntity<EntityCreatedResponse>(entityCreatedResponse, HttpStatus.CREATED);
    }

}
