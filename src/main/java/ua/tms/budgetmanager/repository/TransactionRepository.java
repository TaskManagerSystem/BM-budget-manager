package ua.tms.budgetmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.tms.budgetmanager.data.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
