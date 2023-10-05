package com.bank.podo.domain.account.controller;

import com.bank.podo.domain.account.dto.*;
import com.bank.podo.domain.account.service.AccountAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account/admin")
public class AccountAdminController {
    private final AccountAdminService accountAdminService;

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@RequestBody AdminTransferDTO transferDTO) {
        accountAdminService.transfer(transferDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/balance")
    public ResponseEntity<AdminBalanceDTO> balance(@RequestBody AdminBalanceDTO balanceDTO) {
        balanceDTO = accountAdminService.balance(balanceDTO);
        return ResponseEntity.ok(balanceDTO);
    }

    @PostMapping("/history")
    public ResponseEntity<List<TransactionHistoryDTO>> history(@RequestBody AdminHistoryDTO historyDTO) {
        List<TransactionHistoryDTO> transactionHistoryDTO = accountAdminService.history(historyDTO);
        return ResponseEntity.ok(transactionHistoryDTO);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Void> withdraw(@RequestBody WithdrawDTO withdrawDTO) {
        accountAdminService.withdraw(withdrawDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/deposit")
    public ResponseEntity<Void> deposit(@RequestBody DepositDTO depositDTO) {
        accountAdminService.deposit(depositDTO);
        return ResponseEntity.ok().build();
    }

}
