package ua.tms.budgetmanager.data.dto.wallet;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;
import ua.tms.budgetmanager.data.enumariton.Currency;
import ua.tms.budgetmanager.data.enumariton.WalletType;

@Data
public class WalletCreateDto {

  @NotBlank(message = "Name is required")
  private String name;

  @NotNull(message = "Balance is required")
  @DecimalMin(value = "0.0", message = "Initial balance cannot be negative")
  private BigDecimal balance;

  @NotNull(message = "Wallet type is required")
  private WalletType walletType;

  @NotNull(message = "Currency is required")
  private Currency currency;
}
