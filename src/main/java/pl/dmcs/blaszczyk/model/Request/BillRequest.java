package pl.dmcs.blaszczyk.model.Request;

public class BillRequest {
    private double electricityCost;
    private double hotWaterCost;
    private double coldWaterCost;
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

