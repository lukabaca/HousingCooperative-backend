package pl.dmcs.blaszczyk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dmcs.blaszczyk.model.Entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
