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
import ua.tms.budgetmanager.data.dto.wallet.WalletCreateDto;
import ua.tms.budgetmanager.data.dto.wallet.WalletListDto;
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
class WalletServiceTest extends BaseUtilTest {

  @Mock
  private WalletRepository walletRepository;

  @Mock
  private WalletMapper walletMapper;

  @InjectMocks
  private WalletService walletService;

  private User testUser;
  private Wallet testWallet;
  private WalletCreateDto testWalletCreateDto;

  @BeforeEach
  void setUp() {
    testUser = User.builder()
        .id(userId)
        .username("testuser")
        .password("password")
        .build();

    testWallet = Wallet.builder()
        .id(walletId)
        .name(walletName)
        .balance(walletBalance)
        .walletType(CASH)
        .currency(USD)
        .user(testUser)
        .build();

    testWalletCreateDto = new WalletCreateDto();
    testWalletCreateDto.setName(walletName);
    testWalletCreateDto.setBalance(walletBalance);
    testWalletCreateDto.setWalletType(CASH);
    testWalletCreateDto.setCurrency(USD);
  }

  @Test
  @DisplayName("Should return wallet when found by user ID and wallet ID")
  void getByUserIdAndWalletId_ShouldReturnWallet_WhenWalletExists() {
    when(walletRepository.findByIdAndUserId(walletId, userId))
        .thenReturn(Optional.of(testWallet));

    Wallet result = walletService.getByUserIdAndWalletId(userId, walletId);

    assertNotNull(result);
    assertEquals(walletId, result.getId());
    assertEquals(walletName, result.getName());
    assertEquals(walletBalance, result.getBalance());
    verify(walletRepository).findByIdAndUserId(walletId, userId);
  }

  @Test
  @DisplayName("Should throw EntityNotFoundException when wallet not found by user ID and wallet ID")
  void getByUserIdAndWalletId_ShouldThrowException_WhenWalletNotFound() {
    when(walletRepository.findByIdAndUserId(walletId, userId))
        .thenReturn(Optional.empty());

    EntityNotFoundException exception = assertThrows(
        EntityNotFoundException.class,
        () -> walletService.getByUserIdAndWalletId(userId, walletId)
    );

    assertEquals("Wallet with id %s for user with id %s not found".formatted(walletId, userId),
        exception.getMessage());
    verify(walletRepository).findByIdAndUserId(walletId, userId);
  }

  @Test
  @DisplayName("Should create and return wallet DTO when creating wallet")
  void createWallet_ShouldReturnWalletDto_WhenWalletCreated() {
    Wallet expectedWallet = Wallet.builder()
        .name(walletName)
        .balance(walletBalance)
        .walletType(CASH)
        .currency(USD)
        .user(testUser)
        .build();

    when(walletMapper.toModel(testWalletCreateDto, testUser)).thenReturn(expectedWallet);
    when(walletRepository.save(expectedWallet)).thenReturn(testWallet);
    
    WalletListDto expectedListDto = new WalletListDto();
    expectedListDto.setId(walletId);
    expectedListDto.setName(walletName);
    expectedListDto.setBalance(walletBalance);
    expectedListDto.setWalletType(CASH);
    expectedListDto.setCurrency(USD);
    
    when(walletMapper.toListDto(testWallet)).thenReturn(expectedListDto);

    WalletListDto result = walletService.createWallet(testUser, testWalletCreateDto);

    assertNotNull(result);
    assertEquals(walletName, result.getName());
    assertEquals(walletBalance, result.getBalance());
    assertEquals(CASH, result.getWalletType());
    assertEquals(USD, result.getCurrency());
    verify(walletMapper).toModel(testWalletCreateDto, testUser);
    verify(walletRepository).save(expectedWallet);
    verify(walletMapper).toListDto(testWallet);
  }

  @Test
  @DisplayName("Should update and return wallet DTO when updating wallet")
  void updateWallet_ShouldReturnUpdatedWalletDto_WhenWalletExists() {
    WalletCreateDto updatedDto = new WalletCreateDto();
    updatedDto.setName("Updated Wallet");
    updatedDto.setBalance(BigDecimal.valueOf(2000));
    updatedDto.setWalletType(CARD);
    updatedDto.setCurrency(EUR);

    Wallet updatedWallet = Wallet.builder()
        .id(walletId)
        .name("Updated Wallet")
        .balance(BigDecimal.valueOf(2000))
        .walletType(CARD)
        .currency(EUR)
        .user(testUser)
        .build();

    WalletListDto updatedListDto = getExpectedDto(updatedDto);

    when(walletRepository.findByIdAndUserId(walletId, userId))
        .thenReturn(Optional.of(testWallet));
    doNothing().when(walletMapper).updateWalletFromDto(updatedDto, testWallet);
    when(walletRepository.save(testWallet)).thenReturn(updatedWallet);
    when(walletMapper.toListDto(updatedWallet)).thenReturn(updatedListDto);

    WalletListDto result = walletService.updateWallet(userId, walletId, updatedDto);

    assertNotNull(result);
    assertEquals("Updated Wallet", result.getName());
    assertEquals(BigDecimal.valueOf(2000), result.getBalance());
    assertEquals(CARD, result.getWalletType());
    assertEquals(EUR, result.getCurrency());

    verify(walletRepository).findByIdAndUserId(walletId, userId);
    verify(walletMapper).updateWalletFromDto(updatedDto, testWallet);
    verify(walletRepository).save(testWallet);
    verify(walletMapper).toListDto(updatedWallet);
  }

  @Test
  @DisplayName("Should throw EntityNotFoundException when updating non-existent wallet")
  void updateWallet_ShouldThrowException_WhenWalletNotFound() {
    when(walletRepository.findByIdAndUserId(walletId, userId))
        .thenReturn(Optional.empty());

    assertThrows(
        EntityNotFoundException.class,
        () -> walletService.updateWallet(userId, walletId, testWalletCreateDto)
    );

    verify(walletRepository).findByIdAndUserId(walletId, userId);
    verify(walletMapper, never()).updateWalletFromDto(any(), any());
    verify(walletRepository, never()).save(any());
  }

  @Test
  @DisplayName("Should return success message when deleting wallet")
  void deleteWallet_ShouldReturnSuccessMessage_WhenWalletExists() {
    when(walletRepository.findByIdAndUserId(walletId, userId))
        .thenReturn(Optional.of(testWallet));
    doNothing().when(walletRepository).delete(testWallet);

    String result = walletService.deleteWallet(userId, walletId);

    assertEquals("Wallet with id %s deleted".formatted(walletId), result);
    verify(walletRepository).findByIdAndUserId(walletId, userId);
    verify(walletRepository).delete(testWallet);
  }

  @Test
  @DisplayName("Should throw EntityNotFoundException when deleting non-existent wallet")
  void deleteWallet_ShouldThrowException_WhenWalletNotFound() {
    when(walletRepository.findByIdAndUserId(walletId, userId))
        .thenReturn(Optional.empty());

    assertThrows(
        EntityNotFoundException.class,
        () -> walletService.deleteWallet(userId, walletId)
    );

    verify(walletRepository).findByIdAndUserId(walletId, userId);
    verify(walletRepository, never()).delete(any());
  }

  @Test
  @DisplayName("Should return list of wallet DTOs when getting wallets by user")
  void getWalletsByUser_ShouldReturnWalletDtoList_WhenUserHasWalletsId() {
    Wallet wallet2 = Wallet.builder()
        .id(2L)
        .name("Second Wallet")
        .balance(BigDecimal.valueOf(500))
        .walletType(CARD)
        .currency(USD)
        .user(testUser)
        .build();

    WalletListDto walletCreateDto2 = new WalletListDto();
    walletCreateDto2.setName("Second Wallet");
    walletCreateDto2.setBalance(new BigDecimal(500));
    walletCreateDto2.setWalletType(CARD);
    walletCreateDto2.setCurrency(USD);

    List<Wallet> wallets = List.of(testWallet, wallet2);

    when(walletRepository.findAllByUserId(userId)).thenReturn(wallets);
    
    WalletListDto firstWalletDto = getExpectedDto(testWalletCreateDto);
    
    when(walletMapper.toListDto(testWallet)).thenReturn(firstWalletDto);
    when(walletMapper.toListDto(wallet2)).thenReturn(walletCreateDto2);

    List<WalletListDto> result = walletService.getWalletsByUserId(userId);

    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals(walletName, result.get(0).getName());
    assertEquals("Second Wallet", result.get(1).getName());

    verify(walletRepository).findAllByUserId(userId);
    verify(walletMapper, times(2)).toListDto(any(Wallet.class));
  }

  @Test
  @DisplayName("Should return empty list when user has no wallets")
  void getWalletsByUser_ShouldReturnEmptyList_WhenUserHasNoWalletsId() {
    when(walletRepository.findAllByUserId(userId)).thenReturn(List.of());

    List<WalletListDto> result = walletService.getWalletsByUserId(userId);

    assertNotNull(result);
    assertEquals(0, result.size());
    verify(walletRepository).findAllByUserId(userId);
    verify(walletMapper, never()).toListDto(any());
  }

  @Test
  @DisplayName("Should return total balance when getting total balance by user")
  void getTotalBalanceByUser_ShouldReturnTotalBalance_WhenUserIdHasWallets() {
    BigDecimal expectedTotal = BigDecimal.valueOf(1500.00);

    when(walletRepository.getTotalBalanceByUserId(userId)).thenReturn(expectedTotal);

    BigDecimal result = walletService.getTotalBalanceByUserId(userId);

    assertNotNull(result);
    assertEquals(expectedTotal, result);
    verify(walletRepository).getTotalBalanceByUserId(userId);
  }

  @Test
  @DisplayName("Should return zero balance when user has no wallets")
  void getTotalBalanceByUser_ShouldReturnZero_WhenUserIdHasNoWallets() {
    BigDecimal expectedTotal = ZERO;
    when(walletRepository.getTotalBalanceByUserId(userId)).thenReturn(expectedTotal);

    BigDecimal result = walletService.getTotalBalanceByUserId(userId);

    assertNotNull(result);
    assertEquals(expectedTotal, result);
    verify(walletRepository).getTotalBalanceByUserId(userId);
  }

  private WalletListDto getExpectedDto(WalletCreateDto createDto) {
    WalletListDto listDto = new WalletListDto();
    listDto.setId(walletId);
    listDto.setName(createDto.getName());
    listDto.setBalance(createDto.getBalance());
    listDto.setWalletType(createDto.getWalletType());
    listDto.setCurrency(createDto.getCurrency());

    return listDto;
  }
}
