package ua.tms.budgetmanager.data.dto.transaction;

import java.math.BigDecimal;
import lombok.Data;
import ua.tms.budgetmanager.data.enumariton.TransactionCategory;
import ua.tms.budgetmanager.data.enumariton.TransactionType;

@Data
public class TransactionResponseDto {
  private Long id;
  private BigDecimal amount;
  private TransactionCategory category;
  private TransactionType type;
  private String description;
}
