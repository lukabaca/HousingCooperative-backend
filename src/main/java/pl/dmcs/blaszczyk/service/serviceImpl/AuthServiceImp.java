package pl.dmcs.blaszczyk.service.serviceImpl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.dmcs.blaszczyk.model.Entity.AppUser;
import pl.dmcs.blaszczyk.model.Entity.Role;
import pl.dmcs.blaszczyk.model.Entity.UserInfo;
import pl.dmcs.blaszczyk.model.Exception.BadRequestException;
import pl.dmcs.blaszczyk.model.Exception.ResourceNotFoundException;
import pl.dmcs.blaszczyk.model.Exception.UserAlreadyExistException;
import pl.dmcs.blaszczyk.model.Request.RegistrationRequest;
import pl.dmcs.blaszczyk.model.Response.EntityCreatedResponse;
import pl.dmcs.blaszczyk.repository.AppUserRepository;
import pl.dmcs.blaszczyk.repository.RoleRepository;
import pl.dmcs.blaszczyk.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Service
public class AuthServiceImp implements AuthService {

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public EntityCreatedResponse save(RegistrationRequest registrationRequest) {
            if (registrationRequest == null) {
                throw new BadRequestException();
            }
            AppUser appUserTemp = appUserRepository.findByEmail(registrationRequest.getEmail());
            if (appUserTemp != null) {
                throw new UserAlreadyExistException("User with this email exists");
            }
            AppUser appUser = new AppUser();
            appUser.setEmail(registrationRequest.getEmail());
            appUser.setPassword(bCryptPasswordEncoder.encode(registrationRequest.getPassword()));
            Long roleId = registrationRequest.getRoleId();
            Role role = roleRepository.findById(roleId).orElseThrow(() -> new ResourceNotFoundException());
            appUser.setRole(role);
            UserInfo userInfo = new UserInfo();
            userInfo.setName(registrationRequest.getName());
            userInfo.setSurname(registrationRequest.getSurname());
            userInfo.setBirthDate(registrationRequest.getBirthDate());
            appUser.setUserInfo(userInfo);
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
            appUser.setEmail(registrationRequest.getEmail());
            Long userId = appUserRepository.saveAndFlush(appUser).getId();
            return new EntityCreatedResponse(userId);
        }
        return null;
    }

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }
}
