package pl.dmcs.blaszczyk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dmcs.blaszczyk.model.Entity.ActivationToken;

public interface ActivationTokenRepository extends JpaRepository<ActivationToken, Long> {
    ActivationToken findByConfirmationToken(String confirmationToken);
}
