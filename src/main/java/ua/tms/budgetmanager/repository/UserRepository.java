package ua.tms.budgetmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.tms.budgetmanager.data.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByUsername(String username);
}
