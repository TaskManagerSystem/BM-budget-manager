package ua.tms.budgetmanager.service;

import java.math.BigDecimal;

public class BaseUtilTest {

  protected final Long userId = 1L;

  //Wallet
  protected final Long walletId = 1L;
  protected final String walletName = "Test Wallet";
  protected final BigDecimal walletBalance = BigDecimal.valueOf(1000);

  protected final Long walletIdTo = 2L;
  protected final String walletToName = "Test Wallet";
  protected final BigDecimal walletToBalance = BigDecimal.valueOf(1000);

  //Transaction
  protected final Long transactionId = 1L;
  protected final BigDecimal transactionAmount = BigDecimal.valueOf(100);
  protected final String description = "Test transaction";
}
