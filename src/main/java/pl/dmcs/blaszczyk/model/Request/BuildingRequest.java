package pl.dmcs.blaszczyk.model.Request;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;

public class BuildingRequest {

    private int number;
    @NotBlank
    private String address;
    @NotBlank
    private String city;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
