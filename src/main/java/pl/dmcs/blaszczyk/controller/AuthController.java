package pl.dmcs.blaszczyk.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dmcs.blaszczyk.model.Entity.AppUser;
import pl.dmcs.blaszczyk.model.Request.RegistrationRequest;
import pl.dmcs.blaszczyk.model.Response.EntityCreatedResponse;
import pl.dmcs.blaszczyk.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/user")
    public ResponseEntity<EntityCreatedResponse> newUser(@RequestBody RegistrationRequest registrationRequest) {
        EntityCreatedResponse entityCreatedResponse = authService.save(registrationRequest);
        return new ResponseEntity<EntityCreatedResponse>(entityCreatedResponse, HttpStatus.CREATED);
    }

    @GetMapping("user/{id}")
    public ResponseEntity<AppUser> getUser(@PathVariable Long id) {
        AppUser appUser = authService.getUser(id);
        if (appUser != null) {
            return new ResponseEntity<AppUser>(appUser, HttpStatus.OK);
        }
        return new ResponseEntity<AppUser>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("user/{id}")
    public ResponseEntity<EntityCreatedResponse> updateUser(@PathVariable Long id, @RequestBody RegistrationRequest registrationRequest) {
        EntityCreatedResponse entityCreatedResponse = authService.updateUser(registrationRequest, id);
        if (entityCreatedResponse != null) {
            return  new ResponseEntity<EntityCreatedResponse>(entityCreatedResponse, HttpStatus.CREATED);
        }
        return new ResponseEntity<EntityCreatedResponse>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/users")
    public ResponseEntity<List<AppUser>> getUsers() {
       List<AppUser> users = authService.getUsers();
       if (users.isEmpty()) {
           return new ResponseEntity<List<AppUser>>(HttpStatus.NO_CONTENT);
       }
       return new ResponseEntity<List<AppUser>>(users, HttpStatus.OK);
    }

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
//
//    }
}
