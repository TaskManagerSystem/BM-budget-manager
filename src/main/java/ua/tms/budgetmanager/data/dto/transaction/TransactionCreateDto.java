package ua.tms.budgetmanager.data.dto.transaction;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;
import ua.tms.budgetmanager.data.enumariton.TransactionCategory;
import ua.tms.budgetmanager.data.enumariton.TransactionType;

@Data
public class TransactionCreateDto {

  @NotNull(message = "Amount is required")
  @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
  private BigDecimal amount;

  @NotNull(message = "Category is required")
  private TransactionCategory category;

  @NotNull(message = "Transaction type is required")
  private TransactionType type;

  @Size(max = 500, message = "Description is too long")
  private String description;
}
