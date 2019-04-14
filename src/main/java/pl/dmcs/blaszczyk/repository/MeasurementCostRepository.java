package pl.dmcs.blaszczyk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dmcs.blaszczyk.model.Entity.MeasurementCost;

public interface MeasurementCostRepository extends JpaRepository<MeasurementCost, Long> {
}
