package ua.tms.budgetmanager.service;

import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.tms.budgetmanager.data.dto.WalletDto;
import ua.tms.budgetmanager.data.model.User;
import ua.tms.budgetmanager.data.model.Wallet;
import ua.tms.budgetmanager.mapper.WalletMapper;
import ua.tms.budgetmanager.repository.WalletRepository;

import static java.math.BigDecimal.ZERO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.tms.budgetmanager.data.enumariton.Currency.EUR;
import static ua.tms.budgetmanager.data.enumariton.Currency.USD;
import static ua.tms.budgetmanager.data.enumariton.WalletType.CARD;
import static ua.tms.budgetmanager.data.enumariton.WalletType.CASH;

@ExtendWith(MockitoExtension.class)
@DisplayName("Wallet Service Tests")
class WalletServiceTest {

  @Mock
  private WalletRepository walletRepository;

  @Mock
  private WalletMapper walletMapper;

  @InjectMocks
  private WalletService walletService;

  private User testUser;
  private Wallet testWallet;
  private WalletDto testWalletDto;
  private final Long USER_ID = 1L;
  private final Long WALLET_ID = 1L;
  private final String WALLET_NAME = "Test Wallet";
  private final BigDecimal WALLET_BALANCE = BigDecimal.valueOf(1000);

  @BeforeEach
  void setUp() {
    testUser = User.builder()
        .id(USER_ID)
        .username("testuser")
        .password("password")
        .build();

    testWallet = Wallet.builder()
        .id(WALLET_ID)
        .name(WALLET_NAME)
        .balance(WALLET_BALANCE)
        .walletType(CASH)
        .currency(USD)
        .user(testUser)
        .build();

    testWalletDto = new WalletDto();
    testWalletDto.setName(WALLET_NAME);
    testWalletDto.setBalance(WALLET_BALANCE);
    testWalletDto.setWalletType(CASH);
    testWalletDto.setCurrency(USD);
  }

  @Test
  @DisplayName("Should return wallet when found by user ID and wallet ID")
  void getByUserIdAndWalletId_ShouldReturnWallet_WhenWalletExists() {
    when(walletRepository.findByIdAndUserId(WALLET_ID, USER_ID))
        .thenReturn(Optional.of(testWallet));

    Wallet result = walletService.getByUserIdAndWalletId(USER_ID, WALLET_ID);

    assertNotNull(result);
    assertEquals(WALLET_ID, result.getId());
    assertEquals(WALLET_NAME, result.getName());
    assertEquals(WALLET_BALANCE, result.getBalance());
    verify(walletRepository, times(1)).findByIdAndUserId(WALLET_ID, USER_ID);
  }

  @Test
  @DisplayName("Should throw EntityNotFoundException when wallet not found by user ID and wallet ID")
  void getByUserIdAndWalletId_ShouldThrowException_WhenWalletNotFound() {
    when(walletRepository.findByIdAndUserId(WALLET_ID, USER_ID))
        .thenReturn(Optional.empty());

    EntityNotFoundException exception = assertThrows(
        EntityNotFoundException.class,
        () -> walletService.getByUserIdAndWalletId(USER_ID, WALLET_ID)
    );

    assertEquals("Wallet with id %s for user with id %s not found".formatted(WALLET_ID, USER_ID),
        exception.getMessage());
    verify(walletRepository, times(1)).findByIdAndUserId(WALLET_ID, USER_ID);
  }

  @Test
  @DisplayName("Should create and return wallet DTO when creating wallet")
  void createWallet_ShouldReturnWalletDto_WhenWalletCreated() {
    Wallet expectedWallet = Wallet.builder()
        .name(WALLET_NAME)
        .balance(WALLET_BALANCE)
        .walletType(CASH)
        .currency(USD)
        .user(testUser)
        .build();

    when(walletMapper.toModel(testWalletDto, testUser)).thenReturn(expectedWallet);
    when(walletRepository.save(expectedWallet)).thenReturn(testWallet);
    when(walletMapper.toDto(testWallet)).thenReturn(testWalletDto);

    WalletDto result = walletService.createWallet(testUser, testWalletDto);

    assertNotNull(result);
    assertEquals(WALLET_NAME, result.getName());
    assertEquals(WALLET_BALANCE, result.getBalance());
    assertEquals(CASH, result.getWalletType());
    assertEquals(USD, result.getCurrency());
    verify(walletMapper, times(1)).toModel(testWalletDto, testUser);
    verify(walletRepository, times(1)).save(expectedWallet);
    verify(walletMapper, times(1)).toDto(testWallet);
  }

  @Test
  @DisplayName("Should update and return wallet DTO when updating wallet")
  void updateWallet_ShouldReturnUpdatedWalletDto_WhenWalletExists() {
    WalletDto updatedWalletDto = new WalletDto();
    updatedWalletDto.setName("Updated Wallet");
    updatedWalletDto.setBalance(BigDecimal.valueOf(2000));
    updatedWalletDto.setWalletType(CARD);
    updatedWalletDto.setCurrency(EUR);

    Wallet updatedWallet = Wallet.builder()
        .id(WALLET_ID)
        .name("Updated Wallet")
        .balance(BigDecimal.valueOf(2000))
        .walletType(CARD)
        .currency(EUR)
        .user(testUser)
        .build();

    when(walletRepository.findByIdAndUserId(WALLET_ID, USER_ID))
        .thenReturn(Optional.of(testWallet));
    doNothing().when(walletMapper).updateWalletFromDto(updatedWalletDto, testWallet);
    when(walletRepository.save(testWallet)).thenReturn(updatedWallet);
    when(walletMapper.toDto(updatedWallet)).thenReturn(updatedWalletDto);

    WalletDto result = walletService.updateWallet(testUser, WALLET_ID, updatedWalletDto);

    assertNotNull(result);
    assertEquals("Updated Wallet", result.getName());
    assertEquals(BigDecimal.valueOf(2000), result.getBalance());
    assertEquals(CARD, result.getWalletType());
    assertEquals(EUR, result.getCurrency());

    verify(walletRepository).findByIdAndUserId(WALLET_ID, USER_ID);
    verify(walletMapper).updateWalletFromDto(updatedWalletDto, testWallet);
    verify(walletRepository).save(testWallet);
    verify(walletMapper).toDto(updatedWallet);
  }

  @Test
  @DisplayName("Should throw EntityNotFoundException when updating non-existent wallet")
  void updateWallet_ShouldThrowException_WhenWalletNotFound() {
    when(walletRepository.findByIdAndUserId(WALLET_ID, USER_ID))
        .thenReturn(Optional.empty());

    assertThrows(
        EntityNotFoundException.class,
        () -> walletService.updateWallet(testUser, WALLET_ID, testWalletDto)
    );

    verify(walletRepository).findByIdAndUserId(WALLET_ID, USER_ID);
    verify(walletMapper, never()).updateWalletFromDto(any(), any());
    verify(walletRepository, never()).save(any());
  }

  @Test
  @DisplayName("Should return success message when deleting wallet")
  void deleteWallet_ShouldReturnSuccessMessage_WhenWalletExists() {
    when(walletRepository.findByIdAndUserId(WALLET_ID, USER_ID))
        .thenReturn(Optional.of(testWallet));
    doNothing().when(walletRepository).delete(testWallet);

    String result = walletService.deleteWallet(testUser, WALLET_ID);

    assertEquals("Wallet with id %s deleted".formatted(WALLET_ID), result);
    verify(walletRepository).findByIdAndUserId(WALLET_ID, USER_ID);
    verify(walletRepository).delete(testWallet);
  }

  @Test
  @DisplayName("Should throw EntityNotFoundException when deleting non-existent wallet")
  void deleteWallet_ShouldThrowException_WhenWalletNotFound() {
    when(walletRepository.findByIdAndUserId(WALLET_ID, USER_ID))
        .thenReturn(Optional.empty());

    assertThrows(
        EntityNotFoundException.class,
        () -> walletService.deleteWallet(testUser, WALLET_ID)
    );

    verify(walletRepository).findByIdAndUserId(WALLET_ID, USER_ID);
    verify(walletRepository, never()).delete(any());
  }

  @Test
  @DisplayName("Should return list of wallet DTOs when getting wallets by user")
  void getWalletsByUser_ShouldReturnWalletDtoList_WhenUserHasWallets() {
    Wallet wallet2 = Wallet.builder()
        .id(2L)
        .name("Second Wallet")
        .balance(BigDecimal.valueOf(500))
        .walletType(CARD)
        .currency(USD)
        .user(testUser)
        .build();

    WalletDto walletDto2 = new WalletDto();
    walletDto2.setName("Second Wallet");
    walletDto2.setBalance(new BigDecimal(500));
    walletDto2.setWalletType(CARD);
    walletDto2.setCurrency(USD);

    List<Wallet> wallets = List.of(testWallet, wallet2);

    when(walletRepository.findAllByUserId(USER_ID)).thenReturn(wallets);
    when(walletMapper.toDto(testWallet)).thenReturn(testWalletDto);
    when(walletMapper.toDto(wallet2)).thenReturn(walletDto2);

    List<WalletDto> result = walletService.getWalletsByUser(testUser);

    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals(WALLET_NAME, result.get(0).getName());
    assertEquals("Second Wallet", result.get(1).getName());

    verify(walletRepository).findAllByUserId(USER_ID);
    verify(walletMapper, times(2)).toDto(any(Wallet.class));
  }

  @Test
  @DisplayName("Should return empty list when user has no wallets")
  void getWalletsByUser_ShouldReturnEmptyList_WhenUserHasNoWallets() {
    when(walletRepository.findAllByUserId(USER_ID)).thenReturn(List.of());

    List<WalletDto> result = walletService.getWalletsByUser(testUser);

    assertNotNull(result);
    assertEquals(0, result.size());
    verify(walletRepository).findAllByUserId(USER_ID);
    verify(walletMapper, never()).toDto(any());
  }

  @Test
  @DisplayName("Should return total balance when getting total balance by user")
  void getTotalBalanceByUser_ShouldReturnTotalBalance_WhenUserHasWallets() {
    BigDecimal expectedTotal = BigDecimal.valueOf(1500.00);

    when(walletRepository.getTotalBalanceByUserId(USER_ID)).thenReturn(expectedTotal);

    BigDecimal result = walletService.getTotalBalanceByUser(testUser);

    assertNotNull(result);
    assertEquals(expectedTotal, result);
    verify(walletRepository).getTotalBalanceByUserId(USER_ID);
  }

  @Test
  @DisplayName("Should return zero balance when user has no wallets")
  void getTotalBalanceByUser_ShouldReturnZero_WhenUserHasNoWallets() {
    BigDecimal expectedTotal = ZERO;
    when(walletRepository.getTotalBalanceByUserId(USER_ID)).thenReturn(expectedTotal);

    BigDecimal result = walletService.getTotalBalanceByUser(testUser);

    assertNotNull(result);
    assertEquals(expectedTotal, result);
    verify(walletRepository).getTotalBalanceByUserId(USER_ID);
  }
}
