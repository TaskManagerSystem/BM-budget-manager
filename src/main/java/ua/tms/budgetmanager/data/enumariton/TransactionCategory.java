package ua.tms.budgetmanager.data.enumariton;

import java.io.Serializable;
import java.util.List;

public enum TransactionCategory implements Serializable {
  // Incomes
  INCOME,
  SALARY,

  // Expenses
  LIVING,
  FOOD,
  TRANSPORT,
  ENTERTAINMENT,
  HEALTH,
  SHOPPING,

  OTHER;

  public static List<TransactionCategory> getCategoriesByTransactionType(TransactionType type) {
    return switch (type) {
      case INCOME -> List.of(INCOME, SALARY, OTHER);
      case EXPENSE -> List.of( LIVING, FOOD, TRANSPORT, ENTERTAINMENT, HEALTH, SHOPPING, OTHER);
    };
  }
}
