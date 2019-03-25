package pl.dmcs.blaszczyk.service.serviceIMPL;

import pl.dmcs.blaszczyk.model.Entity.AppUser;
import pl.dmcs.blaszczyk.model.Request.RegistrationRequest;
import pl.dmcs.blaszczyk.model.Response.EntityCreatedResponse;
import pl.dmcs.blaszczyk.repository.AppUserRepository;
import pl.dmcs.blaszczyk.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

@Service
public class AuthServiceImp implements AuthService {

    @Autowired
    AppUserRepository appUserRepository;

    @Override
    public EntityCreatedResponse save(RegistrationRequest registrationRequest) {
            AppUser appUser = new AppUser();
            appUser.setUsername(registrationRequest.getFirstName());
            appUser.setPassword(registrationRequest.getPassword());
            Long userId = appUserRepository.saveAndFlush(appUser).getId();
            return new EntityCreatedResponse(userId);
    }

    @Override
    public List<AppUser> getUsers() {
        return appUserRepository.findAll();
    }

    @Override
    public AppUser getUser(Long id) {
        Optional<AppUser> appUserOptional = appUserRepository.findById(id);
        if (appUserOptional.isPresent()) {
            return appUserOptional.get();
        }
        return null;
    }

    @Override
    public EntityCreatedResponse updateUser(RegistrationRequest registrationRequest, Long id) {
        AppUser appUser = getUser(id);
        if (appUser != null) {
            appUser.setUsername(registrationRequest.getFirstName());
            Long userId = appUserRepository.saveAndFlush(appUser).getId();
            return new EntityCreatedResponse(userId);
        }
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser applicationUser = appUserRepository.findByUsername(username);
        if (applicationUser == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(applicationUser.getUsername(), applicationUser.getPassword(), emptyList());
    }
}
