package pl.dmcs.blaszczyk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dmcs.blaszczyk.model.Entity.Premise;

public interface PremiseRepository extends JpaRepository<Premise, Long> {
}
