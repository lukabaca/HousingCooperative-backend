package pl.dmcs.blaszczyk.service;
import org.springframework.security.core.userdetails.UserDetails;
import pl.dmcs.blaszczyk.model.Entity.ActivationToken;
import pl.dmcs.blaszczyk.model.Entity.AppUser;
import org.springframework.security.core.userdetails.UserDetailsService;
import pl.dmcs.blaszczyk.model.Entity.Role;
import pl.dmcs.blaszczyk.model.Request.RegistrationRequest;
import pl.dmcs.blaszczyk.model.Response.EntityCreatedResponse;
import java.util.List;

public interface AuthService {
    EntityCreatedResponse save(RegistrationRequest registrationRequest);
    List<AppUser> getUsers(Long roleId);
    AppUser getUser(Long id);
    EntityCreatedResponse updateUser(RegistrationRequest registrationRequest, Long id);
    List<Role> getRoles();
    void activateAccount(ActivationToken activationToken);
    void sendActivationToken(Long userId);
}
