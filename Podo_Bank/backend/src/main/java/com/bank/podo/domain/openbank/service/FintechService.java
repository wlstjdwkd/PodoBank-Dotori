package com.bank.podo.domain.openbank.service;

import com.bank.podo.domain.account.entity.Account;
import com.bank.podo.domain.account.entity.TransactionHistory;
import com.bank.podo.domain.account.enums.TransactionType;
import com.bank.podo.domain.account.exception.AccountNotFoundException;
import com.bank.podo.domain.account.exception.InsufficientBalanceException;
import com.bank.podo.domain.account.repository.AccountRepository;
import com.bank.podo.domain.account.repository.TransactionHistoryRepository;
import com.bank.podo.domain.openbank.dto.FintechOneCentVerificationCheckDTO;
import com.bank.podo.domain.openbank.dto.FintechOneCentVerificationDTO;
import com.bank.podo.domain.openbank.entity.AccountVerificationCode;
import com.bank.podo.domain.openbank.entity.FTService;
import com.bank.podo.domain.openbank.entity.FTUser;
import com.bank.podo.domain.openbank.exception.FintechServiceNotFoundException;
import com.bank.podo.domain.openbank.exception.VerificationCodeNotMathchException;
import com.bank.podo.domain.openbank.repository.AccountVerificationCodeRepository;
import com.bank.podo.domain.openbank.repository.FintechRepository;
import com.bank.podo.domain.openbank.repository.ServiceRepository;
import com.bank.podo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FintechService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final FintechRepository fintechRepository;
    private final ServiceRepository serviceRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;
    private final AccountVerificationCodeRepository accountVerificationCodeRepository;

    @Transactional
    public void oneCentVerification(FintechOneCentVerificationDTO fintechOneCentVerificationDTO) {
        FTService FTService = serviceRepository.findByServiceCode(fintechOneCentVerificationDTO.getFintechServiceCode())
                .orElseThrow(() -> new FintechServiceNotFoundException("존재하지 않는 서비스입니다."));

        Account userAccount = accountRepository.findByAccountNumberAndMaturityAtIsNull(fintechOneCentVerificationDTO.getAccount())
                .orElseThrow(() -> new AccountNotFoundException("존재하지 않는 계좌입니다."));

        Account ptAccount = accountRepository.findByAccountNumberAndMaturityAtIsNull(FTService.getAccount().getAccountNumber())
                .orElseThrow(() -> new FintechServiceNotFoundException("존재하지 않는 계좌입니다."));

        BigDecimal transferAmount = BigDecimal.ONE;

        if(ptAccount.getBalance().compareTo(transferAmount) < 0) {
            throw new InsufficientBalanceException("잔액이 부족합니다.");
        }

        String verificationCode = generateVerificationCode(FTService.getServiceName());

        ptAccount.withdraw(transferAmount);
        userAccount.deposit(transferAmount);

        accountRepository.save(ptAccount);
        accountRepository.save(userAccount);

        TransactionHistory senderAccountHistory = TransactionHistory.builder()
                .transactionType(TransactionType.TRANSFER)
                .amount(transferAmount.negate())
                .balanceAfter(ptAccount.getBalance())
                .counterAccount(userAccount)
                .account(ptAccount)
                .content(verificationCode)
                .build();
        TransactionHistory receiverAccountHistory = TransactionHistory.builder()
                .transactionType(TransactionType.TRANSFER)
                .amount(transferAmount)
                .balanceAfter(userAccount.getBalance())
                .counterAccount(ptAccount)
                .account(userAccount)
                .content("1원 인증 출금")
                .build();
        transactionHistoryRepository.save(senderAccountHistory);
        transactionHistoryRepository.save(receiverAccountHistory);

        accountVerificationCodeRepository.save(AccountVerificationCode.builder()
                .accountNumber(userAccount.getAccountNumber())
                .code(verificationCode)
                .build());
    }

    public String oneCentVerificationCheck(FintechOneCentVerificationCheckDTO fintechOneCentVerificationCheckDTO) {
        FTService FTService = serviceRepository.findByServiceCode(fintechOneCentVerificationCheckDTO.getFintechServiceCode())
                .orElseThrow(() -> new FintechServiceNotFoundException("존재하지 않는 서비스입니다."));

        Account userAccount = accountRepository.findByAccountNumberAndMaturityAtIsNull(fintechOneCentVerificationCheckDTO.getAccount())
                .orElseThrow(() -> new AccountNotFoundException("존재하지 않는 계좌입니다."));

        AccountVerificationCode accountVerificationCode = accountVerificationCodeRepository.findById(userAccount.getAccountNumber())
                .orElseThrow(() -> new FintechServiceNotFoundException("존재하지 않는 계좌입니다."));

        if(!accountVerificationCode.getCode().equals(fintechOneCentVerificationCheckDTO.getVerificationCode())) {
            throw new VerificationCodeNotMathchException("인증 코드가 일치하지 않습니다.");
        }

        String userFintechCode = generateFintechUserCode();

        fintechRepository.save(FTUser.builder()
                .fintechCode(userFintechCode)
                .locked(false)
                .ftService(FTService)
                .account(userAccount)
                .build());

        return userFintechCode;
    }

    private String generateFintechUserCode() {
        UUID uuid = UUID.randomUUID();

        String fintechUserCode = Long.toString(uuid.node());

        return fintechUserCode;
    }

    private String generateVerificationCode(String serviceName) {
        StringBuilder code = new StringBuilder();
        code.append(serviceName);

        Random random = new Random();

        for (int i = 0; i < 4; i++) {
            int randomNumber = random.nextInt(10); // 0부터 9까지의 난수 생성
            code.append(randomNumber);
        }

        return code.toString();
    }
}
