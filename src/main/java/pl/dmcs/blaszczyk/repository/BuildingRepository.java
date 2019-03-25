package pl.dmcs.blaszczyk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dmcs.blaszczyk.model.Entity.Building;

public interface BuildingRepository extends JpaRepository<Building, Long> {
}
