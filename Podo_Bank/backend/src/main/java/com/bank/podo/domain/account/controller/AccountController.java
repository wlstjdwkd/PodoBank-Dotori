package com.bank.podo.domain.account.controller;

import com.bank.podo.domain.account.dto.*;
import com.bank.podo.domain.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountController {

    private final PasswordEncoder passwordEncoder;

    private final AccountService accountService;

    @PostMapping("")
    public ResponseEntity<Void> createAccount(@RequestBody CreateAccountDTO createAccountDTO) {
        accountService.createAccount(createAccountDTO, passwordEncoder);
        return ResponseEntity.ok().build();
    }

    @GetMapping("")
    public ResponseEntity<List<AccountDTO>> getAccountList() {
        List<AccountDTO> accountList = accountService.getAccountList();
        return ResponseEntity.ok(accountList);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable Long accountNumber) {
        AccountDTO account = accountService.getAccount(accountNumber);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/{accountNumber}/history")
    public ResponseEntity<List<TransactionHistoryDTO>> getAccountHistory(@PathVariable Long accountNumber) {
        List<TransactionHistoryDTO> accountHistoryList = accountService.getAccountHistory(accountNumber);
        return ResponseEntity.ok(accountHistoryList);
    }

    @PatchMapping("/password")
    public ResponseEntity<AccountDTO> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        accountService.changePassword(changePasswordDTO, passwordEncoder);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/deposit")
    public ResponseEntity<AccountDTO> deposit(@RequestBody DepositDTO depositDTO) {
        accountService.deposit(depositDTO, passwordEncoder);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/withdraw")
    public ResponseEntity<AccountDTO> withdraw(@RequestBody WithdrawDTO withdrawDTO) {
        accountService.withdraw(withdrawDTO, passwordEncoder);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/transfer")
    public ResponseEntity<AccountDTO> transfer(@RequestBody TransferDTO transferDTO) {
        accountService.transfer(transferDTO, passwordEncoder);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("")
    public ResponseEntity<Void> deleteAccount() {
        accountService.deleteAccount();
        return ResponseEntity.ok().build();
    }

}
