package ua.tms.budgetmanager.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.tms.budgetmanager.data.enumariton.Currency;
import ua.tms.budgetmanager.data.enumariton.WalletType;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static java.math.BigDecimal.ZERO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "wallets")
public class Wallet implements Serializable {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "balance", precision = 19, scale = 2, nullable = false)
  @Builder.Default
  private BigDecimal balance = ZERO;

  @Enumerated(STRING)
  @Column(name = "wallet_type", nullable = false)
  private WalletType walletType;

  @Enumerated(STRING)
  @Column(name = "currency", nullable = false)
  private Currency currency;

  @OneToMany(mappedBy = "wallet", cascade = ALL, fetch = LAZY)
  private List<Transaction> transactions;
}
