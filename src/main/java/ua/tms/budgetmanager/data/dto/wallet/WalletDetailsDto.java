package ua.tms.budgetmanager.data.dto.wallet;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ua.tms.budgetmanager.data.dto.transaction.TransactionResponseDto;

@Data
@EqualsAndHashCode(callSuper = true)
public class WalletDetailsDto extends WalletListDto {
  private List<TransactionResponseDto> transactions;
}
