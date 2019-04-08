package pl.dmcs.blaszczyk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dmcs.blaszczyk.model.Entity.Bill;

public interface BillRepository extends JpaRepository<Bill, Long> {
}
