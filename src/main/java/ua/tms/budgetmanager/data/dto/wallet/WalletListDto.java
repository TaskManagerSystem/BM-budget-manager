package ua.tms.budgetmanager.data.dto.wallet;

import java.math.BigDecimal;
import lombok.Data;
import ua.tms.budgetmanager.data.enumariton.Currency;
import ua.tms.budgetmanager.data.enumariton.WalletType;

@Data
public class WalletListDto {
  private Long id;
  private String name;
  private BigDecimal balance;
  private WalletType walletType;
  private Currency currency;
}
