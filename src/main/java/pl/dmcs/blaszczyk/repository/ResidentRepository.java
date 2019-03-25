package pl.dmcs.blaszczyk.repository;
import pl.dmcs.blaszczyk.model.Entity.Resident;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResidentRepository extends JpaRepository<Resident, Long> {

}
