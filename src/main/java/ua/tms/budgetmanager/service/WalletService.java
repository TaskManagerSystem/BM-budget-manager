package ua.tms.budgetmanager.service;

import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.tms.budgetmanager.data.dto.transaction.TransactionTransferDto;
import ua.tms.budgetmanager.data.dto.wallet.WalletCreateDto;
import ua.tms.budgetmanager.data.dto.wallet.WalletDetailsDto;
import ua.tms.budgetmanager.data.dto.wallet.WalletListDto;
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

    public WalletDetailsDto getWalletDetailsById(final Long userId, final Long walletId) {
        return walletMapper.toDetailsDto(getByUserIdAndWalletId(userId, walletId));
    }

    public WalletListDto createWallet(final User user, final WalletCreateDto walletCreateDto) {
        return walletMapper.toListDto(walletRepository.save(walletMapper.toModel(walletCreateDto, user)));
    }

    public WalletListDto updateWallet(final Long userId, final Long walletId, final WalletCreateDto walletCreateDto) {
        Wallet wallet = getByUserIdAndWalletId(userId, walletId);
        walletMapper.updateWalletFromDto(walletCreateDto, wallet);

        return walletMapper.toListDto(walletRepository.save(wallet));
    }

    public String deleteWallet(final Long userId, final Long walletId) {
        Wallet wallet = getByUserIdAndWalletId(userId, walletId);
        walletRepository.delete(wallet);

        return "Wallet with id %s deleted".formatted(walletId);
    }

    public List<WalletListDto> getWalletsByUserId(final Long userId) {
        return walletRepository.findAllByUserId(userId).stream()
                .map(walletMapper::toListDto)
                .toList();
    }

    public BigDecimal getTotalBalanceByUserId(final Long userId) {
        return walletRepository.getTotalBalanceByUserId(userId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void decreaseBalance(final Long userId, final TransactionTransferDto transactionTransferDto) {
        int updatedRows = walletRepository.decreaseBalance(transactionTransferDto.getFromWallet(), userId, transactionTransferDto.getAmount());
        if (updatedRows == 0) {
            throw new RuntimeException("Unable to write off the amount");
        }
    }

    @Transactional
    public void increaseBalance(final Long userId, final TransactionTransferDto transactionTransferDto) {
        int updatedRows = walletRepository.increaseBalance(transactionTransferDto.getToWallet(), userId, transactionTransferDto.getAmount());
        if (updatedRows == 0) {
            throw new RuntimeException("Unable to deposit funds");
        }
    }
}
