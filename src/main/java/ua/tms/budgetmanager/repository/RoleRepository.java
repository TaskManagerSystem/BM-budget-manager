package ua.tms.budgetmanager.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.tms.budgetmanager.data.enumariton.RoleName;
import ua.tms.budgetmanager.data.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(RoleName roleName);
}
