package ua.tms.budgetmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.tms.budgetmanager.data.model.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

}
