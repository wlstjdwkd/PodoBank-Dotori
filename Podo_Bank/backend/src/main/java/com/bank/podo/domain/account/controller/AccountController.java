package com.bank.podo.domain.account.controller;

import com.bank.podo.domain.account.dto.*;
import com.bank.podo.domain.account.enums.AccountType;
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

    @Operation(summary = "계좌 종류 조회")
    @GetMapping("/type")
    public ResponseEntity<List<AccountType>> getAccountTypeList() {
        List<AccountType> accountTypeList = accountService.getAccountTypeList();
        return ResponseEntity.ok(accountTypeList);
    }

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

    @Operation(summary = "계좌 소유주 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping("/{accountNumber}")
    public ResponseEntity<String> getAccountOwnerName(@PathVariable String accountNumber) {
        String name = accountService.getAccountOwnerName(accountNumber);
        return ResponseEntity.ok(name);
    }

    @Operation(summary = "계좌 상세 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping("/{accountNumber}/detail")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable String accountNumber) {
        AccountDTO account = accountService.getAccountDetail(accountNumber);
        return ResponseEntity.ok(account);
    }

    @Operation(summary = "계좌 거래 내역 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping("/{accountNumber}/history")
    public ResponseEntity<List<TransactionHistoryDTO>> getAccountHistory(@PathVariable String accountNumber,
                                             @RequestParam int searchMonth, @RequestParam String transactionType,
                                             @RequestParam int sortType, @RequestParam int page) {
        List<TransactionHistoryDTO> accountHistoryList = accountService.getAccountHistory(accountNumber,
                searchMonth, transactionType, sortType, page);
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

    @Operation(summary = "최근 송금 계좌 조회")
    @GetMapping("/{accountNumber}/recent")
    public ResponseEntity<List<RecentAccountDTO>> getRecentAccountList(@PathVariable String accountNumber) {
        List<RecentAccountDTO> recentAccountList = accountService.getRecentTransferAccountList(accountNumber);
        return ResponseEntity.ok(recentAccountList);
    }
}
