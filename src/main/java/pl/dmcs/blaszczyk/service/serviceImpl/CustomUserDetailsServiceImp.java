package pl.dmcs.blaszczyk.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.dmcs.blaszczyk.model.Entity.AppUser;
import pl.dmcs.blaszczyk.model.Exception.ResourceNotFoundException;
import pl.dmcs.blaszczyk.repository.AppUserRepository;
import pl.dmcs.blaszczyk.service.CustomUserDetailsService;

import javax.transaction.Transactional;

@Service
public class CustomUserDetailsServiceImp implements CustomUserDetailsService {

    @Autowired
    AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        AppUser applicationUser = appUserRepository.findByEmail(email);
        if (applicationUser == null) {
            throw new UsernameNotFoundException("User not found");
        }
//        applicationUser.setPassword();
        return applicationUser;
    }

    @Override
    public AppUser loadUserById(Long id) {
        return appUserRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
