package ua.tms.budgetmanager.repo;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.tms.budgetmanager.enums.RoleName;
import ua.tms.budgetmanager.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(RoleName roleName);
}
