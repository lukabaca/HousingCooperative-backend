package pl.dmcs.blaszczyk.model.Request;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;

public class RegistrationRequest {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @Size(max = 50, min = 2)
    @NotBlank
    private String name;

    @Size(max = 50, min = 2)
    @NotBlank
    private String surname;

    @NotNull
    @DateTimeFormat
    private Date birthDate;

    @NotNull
    private Long roleId;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
