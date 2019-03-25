package pl.dmcs.blaszczyk.service.serviceIMPL;

import pl.dmcs.blaszczyk.model.Entity.Resident;
import pl.dmcs.blaszczyk.repository.ResidentRepository;
import pl.dmcs.blaszczyk.service.ResidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResidentServiceImp implements ResidentService {

    @Autowired
    private ResidentRepository residentRepository;

    public List<Resident> getResidents() {
        return residentRepository.findAll();
    }

    public Long createResident(Resident resident) {
        return residentRepository.save(resident).getId();
    }

    public ResponseEntity<Resident> getResident(Long id) {
        Optional<Resident> resident = residentRepository.findById(id);
        if(!resident.isPresent()) {
            return new ResponseEntity<Resident>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Resident>(resident.get(), HttpStatus.OK);
    }
}
