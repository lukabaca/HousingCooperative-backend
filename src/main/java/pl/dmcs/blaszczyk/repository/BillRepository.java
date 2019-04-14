package pl.dmcs.blaszczyk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.dmcs.blaszczyk.model.Entity.Bill;

import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, Long> {
//    @Query("select b.id, b.coldWaterCost, b.electricityCost, b.heatingCost, b.hotWaterCost, b.isPaid from bill b where b.measurement_id = :id")
//    @Query("select b from Bill b where b.measurement_id = :id")
//    Optional<Bill> findByMeasurementId(Long id);
}
