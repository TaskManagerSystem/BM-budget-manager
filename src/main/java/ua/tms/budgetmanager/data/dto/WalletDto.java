package ua.tms.budgetmanager.data.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;
import ua.tms.budgetmanager.data.enumariton.Currency;
import ua.tms.budgetmanager.data.enumariton.WalletType;

@Data
public class WalletDto {

  @NotBlank(message = "Name is required")
  private String name;
  @NotNull(message = "Balance is required")
  private BigDecimal balance;
  @NotNull(message = "Wallet type is required")
  private WalletType walletType;
  @NotNull(message = "Currency is required")
  private Currency currency;
}
