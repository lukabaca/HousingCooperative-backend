package pl.dmcs.blaszczyk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dmcs.blaszczyk.model.Entity.HousingCooperative;

public interface HousingCooperativeRepository extends JpaRepository<HousingCooperative, Long> {
}
