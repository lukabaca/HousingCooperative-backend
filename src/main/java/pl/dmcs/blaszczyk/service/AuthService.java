package pl.dmcs.blaszczyk.service;
import org.springframework.security.core.userdetails.UserDetails;
import pl.dmcs.blaszczyk.model.Entity.AppUser;
import org.springframework.security.core.userdetails.UserDetailsService;
import pl.dmcs.blaszczyk.model.Request.RegistrationRequest;
import pl.dmcs.blaszczyk.model.Response.EntityCreatedResponse;
import java.util.List;

public interface AuthService extends UserDetailsService {
    EntityCreatedResponse save(RegistrationRequest registrationRequest);
    List<AppUser> getUsers();
    AppUser getUser(Long id);
    EntityCreatedResponse updateUser(RegistrationRequest registrationRequest, Long id);
    UserDetails loadUserById(Long id);
}
