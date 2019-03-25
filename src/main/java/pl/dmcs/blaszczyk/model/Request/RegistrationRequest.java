package pl.dmcs.blaszczyk.model.Request;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class RegistrationRequest {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @Size(min = 2, max = 255)
    @NotNull
    private String firstName;

    @Size(min = 2, max = 255)
    @NotNull
    private String lastName;

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
