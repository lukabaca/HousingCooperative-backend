package pl.dmcs.blaszczyk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import pl.dmcs.blaszczyk.model.Entity.Measurement;
import pl.dmcs.blaszczyk.model.Entity.Premise;
import pl.dmcs.blaszczyk.model.Request.BuildingRequest;
import pl.dmcs.blaszczyk.model.Request.PremiseRequest;
import pl.dmcs.blaszczyk.model.Response.EntityCreatedResponse;
import pl.dmcs.blaszczyk.security.JWTTokenProvider;
import pl.dmcs.blaszczyk.service.PremiseService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("premise")
public class PremiseController {

    @Autowired
    PremiseService premiseService;

    @Autowired
    JWTTokenProvider tokenProvider;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping("premises")
    public ResponseEntity<List<Premise>> getPremises() {
        List<Premise> premises = premiseService.getPremises();
        return new ResponseEntity<List<Premise>>(premises, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("premise")
    public ResponseEntity<EntityCreatedResponse> createPremise(@RequestBody PremiseRequest premiseRequest) {
        EntityCreatedResponse entityCreatedResponse = premiseService.createPremise(premiseRequest);
        return new ResponseEntity<EntityCreatedResponse>(entityCreatedResponse, HttpStatus.CREATED);
    }

    @GetMapping("premise/{id}")
    public ResponseEntity<Premise> getPremise(@RequestParam Long id) {
        Premise premise = premiseService.getPremise(id);
        return new ResponseEntity<Premise>(premise, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("premise/{id}")
    public ResponseEntity<EntityCreatedResponse> updatePremise(@RequestParam Long id, @RequestBody PremiseRequest premiseRequest) {
        EntityCreatedResponse entityCreatedResponse = premiseService.updatePremise(id, premiseRequest);
        return new ResponseEntity<EntityCreatedResponse>(entityCreatedResponse, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping("premise/{id}")
    public ResponseEntity<?> deletePremise(@RequestParam Long id) {
        premiseService.deletePremise(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PutMapping("addLocatorToPremises/{premisesId}/{locatorId}")
    public ResponseEntity<?> addLocatorToPremises(@PathVariable Long premisesId, @PathVariable Long locatorId) {
        premiseService.addLocatorToPremises(premisesId, locatorId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @DeleteMapping("deleteLocatorFromPremises/{premisesId}/{locatorId}")
    public ResponseEntity<?> deleteLocatorFromPremises(@PathVariable Long premisesId, @PathVariable Long locatorId) {
        premiseService.deleteLocatorFromPremises(premisesId, locatorId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("getUserPremises")
    public ResponseEntity<Premise> getUserMeasurements(HttpServletRequest request) {
        String token = tokenProvider.getJwtFromRequest(request);
        if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
            Long userId = tokenProvider.getUserIdFromJWT(token);
            Premise userPremise = premiseService.getUserPremise(userId);
            return new ResponseEntity<Premise>(userPremise, HttpStatus.OK);
        }
        return new ResponseEntity<Premise>(HttpStatus.BAD_REQUEST);
    }
}
