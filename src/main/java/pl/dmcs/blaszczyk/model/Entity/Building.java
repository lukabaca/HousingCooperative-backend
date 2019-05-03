package pl.dmcs.blaszczyk.model.Entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int number;
    private String address;
    private String city;

    @OneToOne
    private AppUser manager;

    @ManyToOne
    @JoinColumn
    @JsonManagedReference
    private HousingCooperative housingCooperative;

    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<Premise> premises;

    public AppUser getManager() {
        return manager;
    }

    public void setManager(AppUser manager) {
        this.manager = manager;
    }

    public Set<Premise> getPremises() {
        return premises;
    }

    public void setPremises(Set<Premise> premises) {
        this.premises = premises;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public HousingCooperative getHousingCooperative() {
        return housingCooperative;
    }

    public void setHousingCooperative(HousingCooperative housingCooperative) {
        this.housingCooperative = housingCooperative;
    }
}
