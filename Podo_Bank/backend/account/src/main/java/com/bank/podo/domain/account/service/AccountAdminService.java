package com.bank.podo.domain.account.service;

import com.bank.podo.domain.account.dto.TransferDTO;
import com.bank.podo.domain.account.entity.Account;
import com.bank.podo.domain.account.entity.TransactionHistory;
import com.bank.podo.domain.account.enums.TransactionType;
import com.bank.podo.domain.account.exception.AccountNotFoundException;
import com.bank.podo.domain.account.exception.InsufficientBalanceException;
import com.bank.podo.domain.account.exception.PasswordRetryCountExceededException;
import com.bank.podo.domain.account.repository.AccountRepository;
import com.bank.podo.domain.account.repository.TransactionHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountAdminService {

    private final AccountRepository accountRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;

    @Transactional(noRollbackFor = PasswordRetryCountExceededException.class)
    public void transfer(TransferDTO transferDTO) {
        Account senderAccount = accountRepository.findByAccountNumberAndDeletedFalse(transferDTO.getSenderAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("계좌를 찾을 수 없습니다."));
        Account receiverAccount = accountRepository.findByAccountNumberAndDeletedFalse(transferDTO.getReceiverAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("계좌를 찾을 수 없습니다."));

        BigDecimal transferAmount = transferDTO.getAmount();

        if(senderAccount.getBalance().compareTo(transferAmount) < 0) {
            throw new InsufficientBalanceException("잔액이 부족합니다.");
        }

        senderAccount.withdraw(transferAmount);
        receiverAccount.deposit(transferAmount);

        accountRepository.saveAll(Arrays.asList(senderAccount, receiverAccount));

        TransactionHistory senderAccountHistory = TransactionHistory.builder()
                .transactionType(TransactionType.WITHDRAWAL)
                .amount(transferAmount)
                .balanceAfter(senderAccount.getBalance())
                .counterAccount(receiverAccount)
                .account(senderAccount)
                .content(transferDTO.getSenderContent())
                .build();
        TransactionHistory receiverAccountHistory = TransactionHistory.builder()
                .transactionType(TransactionType.DEPOSIT)
                .amount(transferAmount)
                .balanceAfter(receiverAccount.getBalance())
                .counterAccount(senderAccount)
                .account(receiverAccount)
                .content(transferDTO.getReceiverContent())
                .build();

        transactionHistoryRepository.saveAll(Arrays.asList(senderAccountHistory, receiverAccountHistory));

        logTransfer(senderAccount, receiverAccount, transferAmount);
    }


    private void logTransfer(Account senderAccount, Account receiverAccount, BigDecimal transferAmount) {
        log.info("=====" + "\t"
                + "송금(Admin)" + "\t"
                + "송금 계좌 번호: " + senderAccount.getAccountNumber() + "\t"
                + "수신 계좌 번호: " + receiverAccount.getAccountNumber() + "\t"
                + "송금액: " + transferAmount + "\t"
                + "=====");
    }

}
