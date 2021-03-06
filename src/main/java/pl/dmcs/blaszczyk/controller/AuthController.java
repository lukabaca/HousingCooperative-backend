package pl.dmcs.blaszczyk.controller;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import pl.dmcs.blaszczyk.model.Entity.ActivationToken;
import pl.dmcs.blaszczyk.model.Entity.AppUser;
import pl.dmcs.blaszczyk.model.Entity.Role;
import pl.dmcs.blaszczyk.model.Request.LoginRequest;
import pl.dmcs.blaszczyk.model.Request.RegistrationRequest;
import pl.dmcs.blaszczyk.model.Response.EntityCreatedResponse;
import pl.dmcs.blaszczyk.model.Response.JWTAuthenticationResponse;
import pl.dmcs.blaszczyk.security.JWTConfig;
import pl.dmcs.blaszczyk.security.JWTTokenProvider;
import pl.dmcs.blaszczyk.service.ActivationTokenService;
import pl.dmcs.blaszczyk.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AuthService authService;

    @Autowired
    ActivationTokenService activationTokenService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    JWTTokenProvider tokenProvider;

    @GetMapping("user/{id}")
    public ResponseEntity<AppUser> getUser(@PathVariable Long id) {
        AppUser appUser = authService.getUser(id);
        if (appUser != null) {
            return new ResponseEntity<AppUser>(appUser, HttpStatus.OK);
        }
        return new ResponseEntity<AppUser>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PutMapping("user/{id}")
    public ResponseEntity<EntityCreatedResponse> editUser(@PathVariable Long id, @RequestBody RegistrationRequest registrationRequest) {
        EntityCreatedResponse entityCreatedResponse = authService.updateUser(registrationRequest, id);
        if (entityCreatedResponse != null) {
            return  new ResponseEntity<EntityCreatedResponse>(entityCreatedResponse, HttpStatus.CREATED);
        }
        return new ResponseEntity<EntityCreatedResponse>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("users")
    public ResponseEntity<List<AppUser>> getUsers(@RequestParam(required = false) Long roleId) {
       List<AppUser> users = authService.getUsers(roleId);
       return new ResponseEntity<List<AppUser>>(users, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("register")
    public ResponseEntity<EntityCreatedResponse> newUser(@Valid @RequestBody RegistrationRequest registrationRequest) {
        EntityCreatedResponse entityCreatedResponse = authService.save(registrationRequest);
        return new ResponseEntity<EntityCreatedResponse>(entityCreatedResponse, HttpStatus.CREATED);
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JWTAuthenticationResponse(jwt));
    }

    @GetMapping("userData")
    public ResponseEntity<?> getUserData(HttpServletRequest request) {
        String token = tokenProvider.getJwtFromRequest(request);
        if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
            Long userId = tokenProvider.getUserIdFromJWT(token);
            AppUser currentlyLoggedUser = authService.getUser(userId);
            return new ResponseEntity<AppUser>(currentlyLoggedUser, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("roles")
    public ResponseEntity<List<Role>> getRoles() {
        List<Role> roles = authService.getRoles();
        return new ResponseEntity<List<Role>>(roles, HttpStatus.OK);
    }

    @GetMapping("activateAccount/{activationToken}")
    public ResponseEntity<?> activateAccount(@PathVariable String activationToken) {
        ActivationToken token = activationTokenService.getActivationToken(activationToken);
        authService.activateAccount(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("sendActivationToken/{userId}")
    public ResponseEntity<?> sendActivationToken(@PathVariable Long userId) {
        authService.sendActivationToken(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
