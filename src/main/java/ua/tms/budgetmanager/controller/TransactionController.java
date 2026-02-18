package ua.tms.budgetmanager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.tms.budgetmanager.data.dto.transaction.TransactionCreateDto;
import ua.tms.budgetmanager.data.dto.transaction.TransactionTransferDto;
import ua.tms.budgetmanager.data.model.User;
import ua.tms.budgetmanager.service.TransactionService;

@RestController
@RequestMapping("/wallets/{walletId}/transactions")
@RequiredArgsConstructor
public class TransactionController {

  private final TransactionService transactionService;

  @GetMapping
  public ResponseEntity<?> getTransactionsByWallet(
      @AuthenticationPrincipal final User user,
      @PathVariable final Long walletId
  ) {
    return ResponseEntity.ok(transactionService.getByWalletIdAndTransactionId(user.getId(), walletId));
  }

  @PostMapping
  public ResponseEntity<?> createTransaction(
      @AuthenticationPrincipal final User user,
      @PathVariable final Long walletId,
      @RequestBody @Valid final TransactionCreateDto transactionCreateDto
  ) {
    return ResponseEntity.ok(transactionService.createTransaction(user.getId(),  walletId, transactionCreateDto));
  }

  @PostMapping("/transfer")
  public ResponseEntity<?> createTransfer(@AuthenticationPrincipal final User user,
                                          @RequestBody final TransactionTransferDto transactionTransferDto) {
    return ResponseEntity.ok(transactionService.createTransferTransaction(user.getId(), transactionTransferDto));
  }

  @PutMapping("/{transactionId}")
  public ResponseEntity<?> updateTransaction(
      @AuthenticationPrincipal final User user,
      @PathVariable final Long walletId,
      @PathVariable final Long transactionId,
      @RequestBody @Valid final TransactionCreateDto transactionCreateDto
  ) {
    return ResponseEntity.ok(transactionService.updateTransaction(user.getId(), walletId, transactionId, transactionCreateDto));
  }

  @DeleteMapping("/{transactionId}")
  public ResponseEntity<?> deleteTransaction(
      @AuthenticationPrincipal final User user,
      @PathVariable final Long walletId,
      @PathVariable final Long transactionId
  ) {
    return ResponseEntity.ok(transactionService.deleteTransaction(user.getId(), walletId, transactionId));
  }
}
