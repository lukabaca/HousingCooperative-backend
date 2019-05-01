package pl.dmcs.blaszczyk.model.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;

@Entity(name="role")
public class Role {
    @Id
    private Long id;
    private String value;
    private String name;

    @OneToMany(mappedBy="role", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonBackReference
    private List<AppUser> appUser;

    public List<AppUser> getAppUser() {
        return appUser;
    }

    public void setAppUser(List<AppUser> appUser) {
        this.appUser = appUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
