package ua.tms.budgetmanager.service;

import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.tms.budgetmanager.data.dto.transaction.TransactionCreateDto;
import ua.tms.budgetmanager.data.dto.transaction.TransactionResponseDto;
import ua.tms.budgetmanager.data.model.Transaction;
import ua.tms.budgetmanager.data.model.Wallet;
import ua.tms.budgetmanager.mapper.TransactionMapper;
import ua.tms.budgetmanager.repository.TransactionRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.tms.budgetmanager.data.enumariton.TransactionCategory.FOOD;
import static ua.tms.budgetmanager.data.enumariton.TransactionType.EXPENSE;

@ExtendWith(MockitoExtension.class)
@DisplayName("Transaction Service Tests")
class TransactionServiceTest extends BaseUtilTest {

  @Mock
  private TransactionRepository transactionRepository;

  @Mock
  private WalletService walletService;

  @Mock
  private TransactionMapper transactionMapper;

  @InjectMocks
  private TransactionService transactionService;

  private Wallet testWallet;
  private Transaction testTransaction;
  private TransactionCreateDto testTransactionCreateDto;
  private TransactionResponseDto testTransactionResponseDto;

  @BeforeEach
  void setUp() {
    testWallet = Wallet.builder()
        .id(walletId)
        .name("Test Wallet")
        .balance(BigDecimal.valueOf(1000))
        .build();

    testTransaction = Transaction.builder()
        .id(transactionId)
        .amount(transactionAmount)
        .category(FOOD)
        .type(EXPENSE)
        .description(description)
        .wallet(testWallet)
        .build();

    testTransactionCreateDto = new TransactionCreateDto();
    testTransactionCreateDto.setAmount(transactionAmount);
    testTransactionCreateDto.setCategory(FOOD);
    testTransactionCreateDto.setType(EXPENSE);
    testTransactionCreateDto.setDescription(description);

    testTransactionResponseDto = new TransactionResponseDto();
    testTransactionResponseDto.setId(transactionId);
    testTransactionResponseDto.setAmount(transactionAmount);
    testTransactionResponseDto.setCategory(FOOD);
    testTransactionResponseDto.setType(EXPENSE);
    testTransactionResponseDto.setDescription(description);
  }

  @Test
  @DisplayName("Should return transaction when found by wallet ID and transaction ID")
  void getByWalletIdAndTransactionId_ShouldReturnTransaction_WhenTransactionExists() {
    when(transactionRepository.findByIdAndWalletId(transactionId, walletId))
        .thenReturn(Optional.of(testTransaction));

    Transaction result = transactionService.getByWalletIdAndTransactionId(walletId, transactionId);

    assertNotNull(result);
    assertEquals(transactionId, result.getId());
    assertEquals(transactionAmount, result.getAmount());
    assertEquals(FOOD, result.getCategory());
    assertEquals(EXPENSE, result.getType());
    assertEquals(description, result.getDescription());
    verify(transactionRepository).findByIdAndWalletId(transactionId, walletId);
  }

  @Test
  @DisplayName("Should throw EntityNotFoundException when transaction not found by wallet ID and transaction ID")
  void getByWalletIdAndTransactionId_ShouldThrowException_WhenTransactionNotFound() {
    when(transactionRepository.findByIdAndWalletId(transactionId, walletId))
        .thenReturn(Optional.empty());

    EntityNotFoundException exception = assertThrows(
        EntityNotFoundException.class,
        () -> transactionService.getByWalletIdAndTransactionId(walletId, transactionId)
    );

    assertEquals("Transaction with id %s for wallet with id %s not found".formatted(transactionId, walletId),
        exception.getMessage());
    verify(transactionRepository).findByIdAndWalletId(transactionId, walletId);
  }

  @Test
  @DisplayName("Should create and return transaction DTO when creating transaction")
  void createTransaction_ShouldReturnTransactionDto_WhenTransactionCreated() {
    Transaction expectedTransaction = Transaction.builder()
        .amount(transactionAmount)
        .category(FOOD)
        .type(EXPENSE)
        .description(description)
        .wallet(testWallet)
        .build();

    when(walletService.getByUserIdAndWalletId(userId, walletId)).thenReturn(testWallet);
    when(transactionMapper.toModel(testTransactionCreateDto, testWallet)).thenReturn(expectedTransaction);
    when(transactionRepository.save(expectedTransaction)).thenReturn(testTransaction);
    when(transactionMapper.toResponseDto(testTransaction)).thenReturn(testTransactionResponseDto);

    TransactionResponseDto result = transactionService.createTransaction(userId, walletId, testTransactionCreateDto);

    assertNotNull(result);
    assertEquals(transactionId, result.getId());
    assertEquals(transactionAmount, result.getAmount());
    assertEquals(FOOD, result.getCategory());
    assertEquals(EXPENSE, result.getType());
    assertEquals(description, result.getDescription());

    verify(walletService).getByUserIdAndWalletId(userId, walletId);
    verify(transactionMapper).toModel(testTransactionCreateDto, testWallet);
    verify(transactionRepository).save(expectedTransaction);
    verify(transactionMapper).toResponseDto(testTransaction);
  }

  @Test
  @DisplayName("Should throw EntityNotFoundException when creating transaction for non-existent wallet")
  void createTransaction_ShouldThrowException_WhenWalletNotFound() {
    when(walletService.getByUserIdAndWalletId(userId, walletId))
        .thenThrow(new EntityNotFoundException("Wallet not found"));

    assertThrows(
        EntityNotFoundException.class,
        () -> transactionService.createTransaction(userId, walletId, testTransactionCreateDto)
    );

    verify(walletService).getByUserIdAndWalletId(userId, walletId);
    verify(transactionMapper, never()).toModel(any(), any());
    verify(transactionRepository, never()).save(any());
  }

  @Test
  @DisplayName("Should update and return transaction DTO when updating transaction")
  void updateTransaction_ShouldReturnUpdatedTransactionDto_WhenTransactionExists() {
    TransactionCreateDto updatedDto = new TransactionCreateDto();
    updatedDto.setAmount(BigDecimal.valueOf(200.00));
    updatedDto.setCategory(FOOD);
    updatedDto.setType(EXPENSE);
    updatedDto.setDescription("Updated transaction");

    Transaction updatedTransaction = Transaction.builder()
        .id(transactionId)
        .amount(BigDecimal.valueOf(200.00))
        .category(FOOD)
        .type(EXPENSE)
        .description("Updated transaction")
        .wallet(testWallet)
        .build();

    TransactionResponseDto updatedResponseDto = new TransactionResponseDto();
    updatedResponseDto.setId(transactionId);
    updatedResponseDto.setAmount(BigDecimal.valueOf(200.00));
    updatedResponseDto.setCategory(FOOD);
    updatedResponseDto.setType(EXPENSE);
    updatedResponseDto.setDescription("Updated transaction");

    when(walletService.getByUserIdAndWalletId(userId, walletId)).thenReturn(testWallet);
    when(transactionRepository.findByIdAndWalletId(transactionId, walletId))
        .thenReturn(Optional.of(testTransaction));
    doNothing().when(transactionMapper).updateTransactionFromDto(updatedDto, testTransaction);
    when(transactionRepository.save(testTransaction)).thenReturn(updatedTransaction);
    when(transactionMapper.toResponseDto(updatedTransaction)).thenReturn(updatedResponseDto);

    TransactionResponseDto result = transactionService.updateTransaction(userId, walletId, transactionId, updatedDto);

    assertNotNull(result);
    assertEquals(transactionId, result.getId());
    assertEquals(BigDecimal.valueOf(200.00), result.getAmount());
    assertEquals("Updated transaction", result.getDescription());

    verify(walletService).getByUserIdAndWalletId(userId, walletId);
    verify(transactionRepository).findByIdAndWalletId(transactionId, walletId);
    verify(transactionMapper).updateTransactionFromDto(updatedDto, testTransaction);
    verify(transactionRepository).save(testTransaction);
    verify(transactionMapper).toResponseDto(updatedTransaction);
  }

  @Test
  @DisplayName("Should throw EntityNotFoundException when updating transaction for non-existent wallet")
  void updateTransaction_ShouldThrowException_WhenWalletNotFound() {
    when(walletService.getByUserIdAndWalletId(userId, walletId))
        .thenThrow(new EntityNotFoundException("Wallet not found"));

    assertThrows(
        EntityNotFoundException.class,
        () -> transactionService.updateTransaction(userId, walletId, transactionId, testTransactionCreateDto)
    );

    verify(walletService).getByUserIdAndWalletId(userId, walletId);
    verify(transactionRepository, never()).findByIdAndWalletId(any(), any());
    verify(transactionMapper, never()).updateTransactionFromDto(any(), any());
    verify(transactionRepository, never()).save(any());
  }

  @Test
  @DisplayName("Should throw EntityNotFoundException when updating non-existent transaction")
  void updateTransaction_ShouldThrowException_WhenTransactionNotFound() {
    when(walletService.getByUserIdAndWalletId(userId, walletId)).thenReturn(testWallet);
    when(transactionRepository.findByIdAndWalletId(transactionId, walletId))
        .thenReturn(Optional.empty());

    assertThrows(
        EntityNotFoundException.class,
        () -> transactionService.updateTransaction(userId, walletId, transactionId, testTransactionCreateDto)
    );

    verify(walletService).getByUserIdAndWalletId(userId, walletId);
    verify(transactionRepository).findByIdAndWalletId(transactionId, walletId);
    verify(transactionMapper, never()).updateTransactionFromDto(any(), any());
    verify(transactionRepository, never()).save(any());
  }

  @Test
  @DisplayName("Should return success message when deleting transaction")
  void deleteTransaction_ShouldReturnSuccessMessage_WhenTransactionExists() {
    when(walletService.getByUserIdAndWalletId(userId, walletId)).thenReturn(testWallet);
    when(transactionRepository.findByIdAndWalletId(transactionId, walletId))
        .thenReturn(Optional.of(testTransaction));
    doNothing().when(transactionRepository).delete(testTransaction);

    String result = transactionService.deleteTransaction(userId, walletId, transactionId);

    assertEquals("Transaction with id %s deleted".formatted(transactionId), result);
    verify(walletService).getByUserIdAndWalletId(userId, walletId);
    verify(transactionRepository).findByIdAndWalletId(transactionId, walletId);
    verify(transactionRepository).delete(testTransaction);
  }

  @Test
  @DisplayName("Should throw EntityNotFoundException when deleting transaction for non-existent wallet")
  void deleteTransaction_ShouldThrowException_WhenWalletNotFound() {
    when(walletService.getByUserIdAndWalletId(userId, walletId))
        .thenThrow(new EntityNotFoundException("Wallet not found"));

    assertThrows(
        EntityNotFoundException.class,
        () -> transactionService.deleteTransaction(userId, walletId, transactionId)
    );

    verify(walletService).getByUserIdAndWalletId(userId, walletId);
    verify(transactionRepository, never()).findByIdAndWalletId(any(), any());
    verify(transactionRepository, never()).delete(any());
  }

  @Test
  @DisplayName("Should throw EntityNotFoundException when deleting non-existent transaction")
  void deleteTransaction_ShouldThrowException_WhenTransactionNotFound() {
    when(walletService.getByUserIdAndWalletId(userId, walletId)).thenReturn(testWallet);
    when(transactionRepository.findByIdAndWalletId(transactionId, walletId))
        .thenReturn(Optional.empty());

    assertThrows(
        EntityNotFoundException.class,
        () -> transactionService.deleteTransaction(userId, walletId, transactionId)
    );

    verify(walletService).getByUserIdAndWalletId(userId, walletId);
    verify(transactionRepository).findByIdAndWalletId(transactionId, walletId);
    verify(transactionRepository, never()).delete(any());
  }
}
