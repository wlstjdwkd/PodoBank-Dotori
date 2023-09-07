package com.bank.podo.domain.account.controller;

import com.bank.podo.domain.account.dto.*;
import com.bank.podo.domain.account.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "계좌 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PostMapping("")
    public ResponseEntity<Void> createAccount(@RequestBody CreateAccountDTO createAccountDTO) {
        accountService.createAccount(createAccountDTO, passwordEncoder);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "계좌 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping("")
    public ResponseEntity<List<AccountDTO>> getAccountList() {
        List<AccountDTO> accountList = accountService.getAccountList();
        return ResponseEntity.ok(accountList);
    }

    @Operation(summary = "계좌 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable Long accountNumber) {
        AccountDTO account = accountService.getAccount(accountNumber);
        return ResponseEntity.ok(account);
    }

    @Operation(summary = "계좌 거래 내역 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping("/{accountNumber}/history")
    public ResponseEntity<List<TransactionHistoryDTO>> getAccountHistory(@PathVariable Long accountNumber) {
        List<TransactionHistoryDTO> accountHistoryList = accountService.getAccountHistory(accountNumber);
        return ResponseEntity.ok(accountHistoryList);
    }

    @Operation(summary = "계좌 비밀번호 변경")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PatchMapping("/password/change")
    public ResponseEntity<AccountDTO> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        accountService.changePassword(changePasswordDTO, passwordEncoder);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "계좌 비밀번호 초기화")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PatchMapping("/password/reset")
    public ResponseEntity<AccountDTO> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        accountService.resetPassword(resetPasswordDTO, passwordEncoder);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "계좌 입금")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PatchMapping("/deposit")
    public ResponseEntity<AccountDTO> deposit(@RequestBody DepositDTO depositDTO) {
        accountService.deposit(depositDTO, passwordEncoder);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "계좌 출금")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PatchMapping("/withdraw")
    public ResponseEntity<AccountDTO> withdraw(@RequestBody WithdrawDTO withdrawDTO) {
        accountService.withdraw(withdrawDTO, passwordEncoder);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "계좌 이체")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PatchMapping("/transfer")
    public ResponseEntity<AccountDTO> transfer(@RequestBody TransferDTO transferDTO) {
        accountService.transfer(transferDTO, passwordEncoder);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "계좌 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
    })
    @DeleteMapping("")
    public ResponseEntity<Void> deleteAccount() {
        accountService.deleteAccount();
        return ResponseEntity.ok().build();
    }

}
