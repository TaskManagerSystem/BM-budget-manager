package ua.tms.budgetmanager.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.tms.budgetmanager.data.dto.transaction.TransactionCreateDto;
import ua.tms.budgetmanager.data.dto.transaction.TransactionResponseDto;
import ua.tms.budgetmanager.data.dto.transaction.TransactionTransferDto;
import ua.tms.budgetmanager.data.enumariton.TransactionCategory;
import ua.tms.budgetmanager.data.enumariton.TransactionType;
import ua.tms.budgetmanager.data.model.Transaction;
import ua.tms.budgetmanager.data.model.Wallet;
import ua.tms.budgetmanager.mapper.TransactionMapper;
import ua.tms.budgetmanager.repository.TransactionRepository;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletService walletService;
    private final TransactionMapper transactionMapper;

    public Transaction getByWalletIdAndTransactionId(final Long walletId, final Long transactionId) {
        return transactionRepository.findByIdAndWalletId(transactionId, walletId).orElseThrow(() ->
                new EntityNotFoundException("Transaction with id %s for wallet with id %s not found".formatted(transactionId, walletId))
        );
    }

    public TransactionResponseDto createTransaction(
            final Long userId,
            final Long walletId,
            final TransactionCreateDto dto
    ) {
        Wallet wallet = walletService.getByUserIdAndWalletId(userId, walletId);

        return transactionMapper.toResponseDto(transactionRepository.save(transactionMapper.toModel(dto, wallet)));
    }

    public TransactionResponseDto updateTransaction(
            final Long userId,
            final Long walletId,
            final Long transactionId,
            final TransactionCreateDto dto
    ) {
        walletService.getByUserIdAndWalletId(userId, walletId);

        Transaction transaction = getByWalletIdAndTransactionId(walletId, transactionId);
        transactionMapper.updateTransactionFromDto(dto, transaction);

        return transactionMapper.toResponseDto(transactionRepository.save(transaction));
    }

    public String deleteTransaction(final Long userId, final Long walletId, final Long transactionId) {
        walletService.getByUserIdAndWalletId(userId, walletId);

        Transaction transaction = getByWalletIdAndTransactionId(walletId, transactionId);
        transactionRepository.delete(transaction);

        return "Transaction with id %s deleted".formatted(transactionId);
    }

    @Transactional
    public String createTransferTransaction(final Long userId, final TransactionTransferDto transactionTransferDto) {
        final long fromWalletId = transactionTransferDto.getFromWallet();
        final long toWalletId = transactionTransferDto.getToWallet();

        TransactionCreateDto createTransaction = createTransactionDto(transactionTransferDto);

        Wallet fromWallet = getWallet(userId, fromWalletId);
        walletService.decreaseBalance(userId, transactionTransferDto);
        transactionRepository.save(transactionMapper.toModel(createTransaction, fromWallet));

        Wallet toWallet = getWallet(userId, toWalletId);
        walletService.increaseBalance(userId, transactionTransferDto);
        transactionRepository.save(transactionMapper.toModel(createTransaction, toWallet));

        return "Transfer from wallet %s to wallet %s was successful".formatted(fromWallet, toWallet);
    }

    private Wallet getWallet(final long userId, final long walletId) {
        return walletService.getByUserIdAndWalletId(userId, walletId);
    }

    private TransactionCreateDto createTransactionDto(TransactionTransferDto dto) {
        return TransactionCreateDto.builder()
                .amount(dto.getAmount())
                .category(TransactionCategory.OTHER)
                .description(dto.getDescription())
                .type(TransactionType.TRANSFER)
                .build();
    }
}
