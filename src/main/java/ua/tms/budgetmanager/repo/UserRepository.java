package ua.tms.budgetmanager.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.tms.budgetmanager.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByUsername(String username);
}
