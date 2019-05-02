package pl.dmcs.blaszczyk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.dmcs.blaszczyk.model.Entity.Measurement;

public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
    @Query("Select avg(m.coldWater) from Measurement m")
    Double getAverageValueOfColdWater();

    @Query("Select avg(m.hotWater) from Measurement m")
    Double getAverageValueOfHotWater();

    @Query("Select avg(m.electricity) from Measurement m")
    Double getAverageValueOfElectricity();

    @Query("Select avg(m.heating) from Measurement m")
    Double getAverageValueOfHeating();
}
