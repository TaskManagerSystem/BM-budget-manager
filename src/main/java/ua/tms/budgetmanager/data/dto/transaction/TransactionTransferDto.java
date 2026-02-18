package ua.tms.budgetmanager.data.dto.transaction;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionTransferDto {
    private Long fromWallet;
    private Long toWallet;
    private BigDecimal amount;
    private String description;
}
