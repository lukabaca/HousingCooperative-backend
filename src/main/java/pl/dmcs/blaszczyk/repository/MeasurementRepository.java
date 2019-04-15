package pl.dmcs.blaszczyk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dmcs.blaszczyk.model.Entity.Measurement;

public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
}
