package pl.dmcs.blaszczyk.service.serviceImpl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import pl.dmcs.blaszczyk.model.Entity.AppUser;
import pl.dmcs.blaszczyk.model.Entity.ActivationToken;
import pl.dmcs.blaszczyk.model.Entity.Role;
import pl.dmcs.blaszczyk.model.Entity.UserInfo;
import pl.dmcs.blaszczyk.model.Exception.ActivationTokenExpiredException;
import pl.dmcs.blaszczyk.model.Exception.BadRequestException;
import pl.dmcs.blaszczyk.model.Exception.ResourceNotFoundException;
import pl.dmcs.blaszczyk.model.Exception.UserAlreadyExistException;
import pl.dmcs.blaszczyk.model.Request.RegistrationRequest;
import pl.dmcs.blaszczyk.model.Response.EntityCreatedResponse;
import pl.dmcs.blaszczyk.repository.AppUserRepository;
import pl.dmcs.blaszczyk.repository.RoleRepository;
import pl.dmcs.blaszczyk.service.ActivationTokenService;
import pl.dmcs.blaszczyk.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.dmcs.blaszczyk.service.MailingService;

import java.util.*;


@Service
@Transactional
public class AuthServiceImp implements AuthService {

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    ActivationTokenService activationTokenService;

    @Autowired
    MailingService mailingService;

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
            Role role = roleRepository.findById(roleId).orElseThrow(() -> new ResourceNotFoundException("role not found"));
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
    public List<AppUser> getUsers(Long roleId) {
        if (roleId == null) {
            return appUserRepository.findAll();
        }
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        List<AppUser> users = appUserRepository.findAll();
        List<AppUser> usersWithRole = new ArrayList<>();
        for (AppUser appUser : users) {
            if (appUser != null) {
                if (appUser.getRole().getId().equals(role.getId())) {
                    usersWithRole.add(appUser);
                }
            }
        }
        return usersWithRole;
    }

    @Override
    public AppUser getUser(Long id) {
        return appUserRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("user not found"));
    }

    @Override
    public EntityCreatedResponse updateUser(RegistrationRequest registrationRequest, Long id) {
        if (registrationRequest == null) {
            throw new BadRequestException();
        }
        AppUser appUser = appUserRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("user not found"));
        appUser.setEmail(registrationRequest.getEmail());
        appUser.getUserInfo().setName(registrationRequest.getName());
        appUser.getUserInfo().setSurname(registrationRequest.getSurname());
        appUser.getUserInfo().setBirthDate(registrationRequest.getBirthDate());
        Role role = roleRepository.findById(registrationRequest.getRoleId()).orElseThrow(() -> new ResourceNotFoundException("role not found"));
        appUser.setRole(role);
        Long userId = appUserRepository.saveAndFlush(appUser).getId();
        return new EntityCreatedResponse(userId);
    }

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Override
    public void activateAccount(ActivationToken activationToken) {
        if (activationToken == null) {
            throw new BadRequestException();
        }
        Date currentDate = new Date();
        if (activationToken.getExpirationDate().compareTo(currentDate) >= 0) {
            AppUser appUser = activationToken.getAppUser();
            if (appUser != null) {
                appUser.setActive(true);
                appUserRepository.save(appUser);
            } else {
                throw new ResourceNotFoundException("App user for token not found");
            }
        } else {
            throw new ActivationTokenExpiredException("Token date has expired");
        }
    }

    @Override
    public void sendActivationToken(Long userId) {
        AppUser appUser = appUserRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found"));
        ActivationToken activationToken = activationTokenService.createActivationTokenForUser();
        activationToken.setAppUser(appUser);
        appUser.getActivationTokens().add(activationToken);
        appUserRepository.save(appUser);
        mailingService.sendMail("lukadmcs@gmail.com", "Link aktywacyjny do spółdzielni", "Link aktywacyjny do konta: " +
                "http://localhost:8080/auth/activateAccount/" + activationToken.getConfirmationToken());
    }

}
