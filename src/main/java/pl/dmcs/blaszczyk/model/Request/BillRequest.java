package pl.dmcs.blaszczyk.model.Request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class BillRequest {
    @Positive
    @NotNull
    private double electricityCost;
    @Positive
    @NotNull
    private double hotWaterCost;
    @Positive
    @NotNull
    private double coldWaterCost;
    @Positive
    @NotNull
    private double heatingCost;

    public double getElectricityCost() {
        return electricityCost;
    }

    public void setElectricityCost(double electricityCost) {
        this.electricityCost = electricityCost;
    }

    public double getHotWaterCost() {
        return hotWaterCost;
    }

    public void setHotWaterCost(double hotWaterCost) {
        this.hotWaterCost = hotWaterCost;
    }

    public double getColdWaterCost() {
        return coldWaterCost;
    }

    public void setColdWaterCost(double coldWaterCost) {
        this.coldWaterCost = coldWaterCost;
    }

    public double getHeatingCost() {
        return heatingCost;
    }

    public void setHeatingCost(double heatingCost) {
        this.heatingCost = heatingCost;
    }
}

