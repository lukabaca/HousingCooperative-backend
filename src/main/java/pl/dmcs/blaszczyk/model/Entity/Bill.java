package pl.dmcs.blaszczyk.model.Entity;

import javax.persistence.*;

@Entity(name="Bill")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private double electricityCost;
    private double hotWaterCost;
    private double coldWaterCost;
    private double heatingCost;
    private boolean isPaid;
    private boolean isChecked;
    private boolean isAccepted;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    @OneToOne
    private Measurement measurement;

    public Measurement getMeasurement() {
        return measurement;
    }

    public void setMeasurement(Measurement measurement) {
        this.measurement = measurement;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
