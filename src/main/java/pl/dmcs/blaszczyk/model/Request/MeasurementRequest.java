package pl.dmcs.blaszczyk.model.Request;

import javax.validation.constraints.Min;

public class MeasurementRequest {
    private double electricity;
    private double hotWater;
    private double coldWater;
    private double heating;
    private int month;
    private int year;
    private Long premisesId;

    public Long getPremisesId() {
        return premisesId;
    }

    public void setPremisesId(Long premisesId) {
        this.premisesId = premisesId;
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

    public double getElectricity() {
        return electricity;
    }

    public void setElectricity(double electricity) {
        this.electricity = electricity;
    }

    public double getHotWater() {
        return hotWater;
    }

    public void setHotWater(double hotWater) {
        this.hotWater = hotWater;
    }

    public double getColdWater() {
        return coldWater;
    }

    public void setColdWater(double coldWater) {
        this.coldWater = coldWater;
    }

    public double getHeating() {
        return heating;
    }

    public void setHeating(double heating) {
        this.heating = heating;
    }
}
