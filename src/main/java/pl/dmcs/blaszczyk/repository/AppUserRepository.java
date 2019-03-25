package pl.dmcs.blaszczyk.repository;
import pl.dmcs.blaszczyk.model.Entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByUsername(String username);
}
