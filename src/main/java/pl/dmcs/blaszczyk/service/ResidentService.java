package pl.dmcs.blaszczyk.service;
import pl.dmcs.blaszczyk.model.Entity.Resident;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface ResidentService {
    ResponseEntity<Resident> getResident(Long id);
    List<Resident> getResidents();
    Long createResident(Resident resident);
}
