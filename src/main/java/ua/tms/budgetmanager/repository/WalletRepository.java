package ua.tms.budgetmanager.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.tms.budgetmanager.data.model.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    List<Wallet> findAllByUserId(Long userId);

    Optional<Wallet> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT SUM(w.balance) FROM Wallet w WHERE w.user.id = :userId")
    BigDecimal getTotalBalanceByUserId(@Param("userId") Long userId);


    @Modifying
    @Query("UPDATE Wallet w SET w.balance = w.balance - :amount " +
            "WHERE w.id = :walletId AND w.user.id = :userId AND w.balance >= :amount")
    int decreaseBalance(
            @Param("walletId") Long walletId,
            @Param("userId") Long userId,
            @Param("amount") BigDecimal amount
    );

    @Modifying
    @Query("UPDATE Wallet w SET w.balance = w.balance + :amount " +
            "WHERE w.id = :walletId AND w.user.id = :userId")
    int increaseBalance(
            @Param("walletId") Long walletId,
            @Param("userId") Long userId,
            @Param("amount") BigDecimal amount
    );


}
