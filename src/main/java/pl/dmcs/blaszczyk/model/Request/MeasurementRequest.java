package pl.dmcs.blaszczyk.model.Request;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

public class MeasurementRequest {
    @Positive
    private Double electricity;
    @Positive
    private Double hotWater;
    @Positive
    private Double coldWater;
    @Positive
    private Double heating;
    private int month;
    private int year;
    private Long premisesId;

    public Double getElectricity() {
        return electricity;
    }

    public void setElectricity(Double electricity) {
        this.electricity = electricity;
    }

    public Double getHotWater() {
        return hotWater;
    }

    public void setHotWater(Double hotWater) {
        this.hotWater = hotWater;
    }

    public Double getColdWater() {
        return coldWater;
    }

    public void setColdWater(Double coldWater) {
        this.coldWater = coldWater;
    }

    public Double getHeating() {
        return heating;
    }

    public void setHeating(Double heating) {
        this.heating = heating;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Long getPremisesId() {
        return premisesId;
    }

    public void setPremisesId(Long premisesId) {
        this.premisesId = premisesId;
    }
}
