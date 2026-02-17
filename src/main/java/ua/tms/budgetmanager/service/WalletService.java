package ua.tms.budgetmanager.service;

import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.tms.budgetmanager.data.dto.WalletDto;
import ua.tms.budgetmanager.data.model.User;
import ua.tms.budgetmanager.data.model.Wallet;
import ua.tms.budgetmanager.mapper.WalletMapper;
import ua.tms.budgetmanager.repository.WalletRepository;

@Service
@RequiredArgsConstructor
public class WalletService {

  private final WalletRepository walletRepository;
  private final WalletMapper walletMapper;

  public Wallet getByUserIdAndWalletId(final Long userId, final Long walletId) {
    return walletRepository.findByIdAndUserId(walletId, userId).orElseThrow(() ->
        new EntityNotFoundException("Wallet with id %s for user with id %s not found".formatted(walletId, userId))
    );
  }

  public WalletDto createWallet(final User user, final WalletDto walletDto) {
    return walletMapper.toDto(walletRepository.save(walletMapper.toModel(walletDto, user)));
  }

  public WalletDto updateWallet(final Long userId, final Long walletId, final WalletDto walletDto) {
    Wallet wallet = getByUserIdAndWalletId(userId, walletId);
    walletMapper.updateWalletFromDto(walletDto, wallet);

    return walletMapper.toDto(walletRepository.save(wallet));
  }

  public String deleteWallet(final Long userId, final Long walletId) {
    Wallet wallet = getByUserIdAndWalletId(userId, walletId);
    walletRepository.delete(wallet);

    return "Wallet with id %s deleted".formatted(walletId);
  }

  public List<WalletDto> getWalletsByUserId(final Long userId) {
    return walletRepository.findAllByUserId(userId).stream()
        .map(walletMapper::toDto)
        .toList();
  }

  public BigDecimal getTotalBalanceByUserId(final Long userId) {
    return walletRepository.getTotalBalanceByUserId(userId);
  }
}
