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
import ua.tms.budgetmanager.data.dto.wallet.WalletCreateDto;
import ua.tms.budgetmanager.data.model.User;
import ua.tms.budgetmanager.service.WalletService;

@RestController
@RequestMapping("/wallets")
@RequiredArgsConstructor
public class WalletController {

  private final WalletService walletService;

  @GetMapping
  public ResponseEntity<?> getWalletsByUser(@AuthenticationPrincipal final User user) {
    return ResponseEntity.ok(walletService.getWalletsByUserId(user.getId()));
  }

  @GetMapping("/{walletId}")
  public ResponseEntity<?> getWalletInfoByUser(
      @AuthenticationPrincipal final User user,
      @PathVariable final Long walletId
  ) {
    return ResponseEntity.ok(walletService.getWalletDetailsById(user.getId(), walletId));
  }


  @GetMapping("/total-balance")
  public ResponseEntity<?> getTotalBalanceByUser(@AuthenticationPrincipal final User user) {
    return ResponseEntity.ok(walletService.getTotalBalanceByUserId(user.getId()));
  }

  @PostMapping
  public ResponseEntity<?> createWallet(
      @AuthenticationPrincipal final User user,
      @RequestBody @Valid final WalletCreateDto walletCreateDto
  ) {
    return ResponseEntity.ok(walletService.createWallet(user, walletCreateDto));
  }

  @PutMapping("/{walletId}")
  public ResponseEntity<?> updateWallet(
      @AuthenticationPrincipal final User user,
      @PathVariable final Long walletId,
      @RequestBody @Valid final WalletCreateDto walletCreateDto
  ) {
    return ResponseEntity.ok(walletService.updateWallet(user.getId(), walletId, walletCreateDto));
  }

  @DeleteMapping("/{walletId}")
  public ResponseEntity<?> deleteUser(
      @AuthenticationPrincipal final User user,
      @PathVariable final Long walletId
  ) {
    return ResponseEntity.ok(walletService.deleteWallet(user.getId(), walletId));
  }
}
