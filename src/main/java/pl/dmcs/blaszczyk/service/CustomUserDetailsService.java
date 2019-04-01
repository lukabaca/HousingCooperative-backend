package pl.dmcs.blaszczyk.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import pl.dmcs.blaszczyk.model.Entity.AppUser;

public interface CustomUserDetailsService extends UserDetailsService {
    AppUser loadUserById(Long id);
}

