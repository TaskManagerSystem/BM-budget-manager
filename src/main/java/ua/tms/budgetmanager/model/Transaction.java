package ua.tms.budgetmanager.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.tms.budgetmanager.data.enumariton.TransactionCategory;
import ua.tms.budgetmanager.data.enumariton.TransactionType;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction implements Serializable {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Column(name = "amount", precision = 19, scale = 2, nullable = false)
  private BigDecimal amount;

  @Enumerated(STRING)
  @Column(name = "category", nullable = false)
  private TransactionCategory category;

  @Enumerated(STRING)
  @Column(name = "type", nullable = false)
  private TransactionType type;

  @Column(name = "description", length = 500)
  private String description;

  @Column(name = "created_at", nullable = false, updatable = false)
  @Builder.Default
  private LocalDateTime createdAt = LocalDateTime.now();
}
