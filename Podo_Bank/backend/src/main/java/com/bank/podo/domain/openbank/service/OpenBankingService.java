package com.bank.podo.domain.openbank.service;

import com.bank.podo.domain.account.dto.TransactionHistoryDTO;
import com.bank.podo.domain.account.entity.Account;
import com.bank.podo.domain.account.entity.TransactionHistory;
import com.bank.podo.domain.account.enums.TransactionType;
import com.bank.podo.domain.account.exception.AccountNotFoundException;
import com.bank.podo.domain.account.exception.InsufficientBalanceException;
import com.bank.podo.domain.account.repository.AccountRepository;
import com.bank.podo.domain.account.repository.TransactionHistoryRepository;
import com.bank.podo.domain.openbank.dto.*;
import com.bank.podo.domain.openbank.entity.AccountVerificationCode;
import com.bank.podo.domain.openbank.entity.FintechUser;
import com.bank.podo.domain.openbank.exception.FintechServiceNotFoundException;
import com.bank.podo.domain.openbank.exception.VerificationCodeNotMathchException;
import com.bank.podo.domain.openbank.repository.AccountVerificationCodeRepository;
import com.bank.podo.domain.openbank.repository.FintechUserRepository;
import com.bank.podo.domain.openbank.repository.ServiceRepository;
import com.bank.podo.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OpenBankingService {

    private final AccountRepository accountRepository;
    private final FintechUserRepository fintechUserRepository;
    private final ServiceRepository serviceRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;
    private final AccountVerificationCodeRepository accountVerificationCodeRepository;

    @Transactional
    public void oneCentVerification(FintechOneCentVerificationDTO fintechOneCentVerificationDTO) {
        System.out.println(fintechOneCentVerificationDTO.getFintechServiceCode());
        com.bank.podo.domain.openbank.entity.FintechService FintechService = serviceRepository.findByServiceCode(fintechOneCentVerificationDTO.getFintechServiceCode())
                .orElseThrow(() -> new FintechServiceNotFoundException("존재하지 않는 서비스입니다."));

        Account userAccount = accountRepository.findByAccountNumberAndDeletedFalse(fintechOneCentVerificationDTO.getAccount())
                .orElseThrow(() -> new AccountNotFoundException("존재하지 않는 계좌입니다."));

        Account ftAccount = accountRepository.findByAccountNumberAndDeletedFalse(FintechService.getAccount().getAccountNumber())
                .orElseThrow(() -> new FintechServiceNotFoundException("존재하지 않는 계좌입니다."));

        BigDecimal transferAmount = BigDecimal.ONE;

        if(ftAccount.getBalance().compareTo(transferAmount) < 0) {
            throw new InsufficientBalanceException("잔액이 부족합니다.");
        }

        String verificationCode = generateVerificationCode(FintechService.getServiceName());

        ftAccount.withdraw(transferAmount);
        userAccount.deposit(transferAmount);

        accountRepository.save(ftAccount);
        accountRepository.save(userAccount);

        TransactionHistory senderAccountHistory = TransactionHistory.builder()
                .transactionType(TransactionType.TRANSFER)
                .amount(transferAmount.negate())
                .balanceAfter(ftAccount.getBalance())
                .counterAccount(userAccount)
                .account(ftAccount)
                .content("1원 인증을 위한 출금")
                .build();
        TransactionHistory receiverAccountHistory = TransactionHistory.builder()
                .transactionType(TransactionType.DEPOSIT)
                .amount(transferAmount)
                .balanceAfter(userAccount.getBalance())
                .counterAccount(ftAccount)
                .account(userAccount)
                .content(verificationCode)
                .build();
        transactionHistoryRepository.save(senderAccountHistory);
        transactionHistoryRepository.save(receiverAccountHistory);

        accountVerificationCodeRepository.save(AccountVerificationCode.builder()
                .accountNumber(userAccount.getAccountNumber())
                .code(verificationCode)
                .build());
    }

    @Transactional
    public UserAccountFintechCodeDTO oneCentVerificationCheck(FintechOneCentVerificationCheckDTO fintechOneCentVerificationCheckDTO) {
        User user = getLoginUser();

        com.bank.podo.domain.openbank.entity.FintechService fintechService = serviceRepository.findByServiceCodeAndUser(fintechOneCentVerificationCheckDTO.getFintechServiceCode(), user)
                .orElseThrow(() -> new FintechServiceNotFoundException("존재하지 않는 서비스입니다."));

        Account userAccount = accountRepository.findByAccountNumberAndDeletedFalse(fintechOneCentVerificationCheckDTO.getAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("존재하지 않는 계좌입니다."));

        AccountVerificationCode accountVerificationCode = accountVerificationCodeRepository.findById(userAccount.getAccountNumber())
                .orElseThrow(() -> new VerificationCodeNotMathchException("인증 정보가 없습니다."));

        if(!accountVerificationCode.getCode().equals(fintechOneCentVerificationCheckDTO.getVerificationCode())) {
            throw new VerificationCodeNotMathchException("인증 코드가 일치하지 않습니다.");
        } else {
            accountVerificationCodeRepository.delete(accountVerificationCode);
        }

        String userFintechCode = generateFintechUserCode();

        fintechUserRepository.save(FintechUser.builder()
                .fintechCode(userFintechCode)
                .locked(false)
                .fintechService(fintechService)
                .account(userAccount)
                .build());

        return UserAccountFintechCodeDTO.builder()
                .fintechCode(userFintechCode)
                .accountNumber(userAccount.getAccountNumber())
                .serviceCode(fintechService.getServiceCode())
                .build();
    }

    @Transactional
    public void withdrawUserAccount(FintechWithdrawDTO fintechWithdrawDTO) {
        User user = getLoginUser();

        com.bank.podo.domain.openbank.entity.FintechService fintechService = serviceRepository.findByServiceCodeAndUser(fintechWithdrawDTO.getServiceCode(), user)
                .orElseThrow(() -> new FintechServiceNotFoundException("존재하지 않는 서비스입니다."));

        FintechUser fintechUser = fintechUserRepository.findByFintechServiceAndFintechCode(fintechService, fintechWithdrawDTO.getFintechCode())
                .orElseThrow(() -> new FintechServiceNotFoundException("핀테크 정보가 없습니다."));

        BigDecimal transferAmount = fintechWithdrawDTO.getAmount();

        transfer(fintechUser.getAccount(), fintechService.getAccount(), transferAmount, fintechWithdrawDTO.getContent());
    }

    @Transactional
    public void depositUserAccount(FintechDepositDTO fintechDepositDTO) {
        User user = getLoginUser();

        com.bank.podo.domain.openbank.entity.FintechService fintechService = serviceRepository.findByServiceCodeAndUser(fintechDepositDTO.getServiceCode(), user)
                .orElseThrow(() -> new FintechServiceNotFoundException("존재하지 않는 서비스입니다."));

        FintechUser fintechUser = fintechUserRepository.findByFintechServiceAndFintechCode(fintechService, fintechDepositDTO.getFintechCode())
                .orElseThrow(() -> new FintechServiceNotFoundException("핀테크 정보가 없습니다."));

        Account receiverAccount = fintechUser.getAccount();

        BigDecimal transferAmount = fintechDepositDTO.getAmount();

        transfer(fintechService.getAccount(), receiverAccount, transferAmount, fintechDepositDTO.getContent());
    }

    @Transactional
    public void transfer(Account senderAccount, Account receiverAccount, BigDecimal amount, String content) {
        if(senderAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("잔액이 부족합니다.");
        }

        senderAccount.withdraw(amount);
        receiverAccount.deposit(amount);

        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);

        TransactionHistory senderAccountHistory = TransactionHistory.builder()
                .transactionType(TransactionType.TRANSFER)
                .amount(amount.negate())
                .balanceAfter(senderAccount.getBalance())
                .counterAccount(receiverAccount)
                .account(senderAccount)
                .content(content)
                .build();
        TransactionHistory receiverAccountHistory = TransactionHistory.builder()
                .transactionType(TransactionType.DEPOSIT)
                .amount(amount)
                .balanceAfter(receiverAccount.getBalance())
                .counterAccount(senderAccount)
                .account(receiverAccount)
                .content(content)
                .build();
        transactionHistoryRepository.save(senderAccountHistory);
        transactionHistoryRepository.save(receiverAccountHistory);
    }

    @Transactional(readOnly = true)
    public FintechUserBalanceDTO getUserAccountBalance(FintechUserDTO fintechUserDTO) {
        User user = getLoginUser();

        com.bank.podo.domain.openbank.entity.FintechService fintechService = serviceRepository.findByServiceCodeAndUser(fintechUserDTO.getServiceCode(), user)
                .orElseThrow(() -> new FintechServiceNotFoundException("존재하지 않는 서비스입니다."));

        FintechUser fintechUser = fintechUserRepository.findByFintechServiceAndFintechCode(fintechService, fintechUserDTO.getFintechCode())
                .orElseThrow(() -> new FintechServiceNotFoundException("핀테크 정보가 없습니다."));

        return FintechUserBalanceDTO.builder()
                .accountNumber(fintechUser.getAccount().getAccountNumber())
                .balance(fintechUser.getAccount().getBalance())
                .build();
    }

    @Transactional(readOnly = true)
    public List<TransactionHistoryDTO> getUserAccountTransactionHistory(FintechUserHistoryDTO fintechUserHistoryDTO) {
        User user = getLoginUser();

        com.bank.podo.domain.openbank.entity.FintechService fintechService = serviceRepository.findByServiceCodeAndUser(fintechUserHistoryDTO.getServiceCode(), user)
                .orElseThrow(() -> new FintechServiceNotFoundException("존재하지 않는 서비스입니다."));

        FintechUser fintechUser = fintechUserRepository.findByFintechServiceAndFintechCode(fintechService, fintechUserHistoryDTO.getFintechCode())
                .orElseThrow(() -> new FintechServiceNotFoundException("핀테크 정보가 없습니다."));

        List<TransactionHistory> transactionHistoryList =
                transactionHistoryRepository.findAllByAccountAndCreatedAtGreaterThanEqual(fintechUser.getAccount(),
                        fintechUserHistoryDTO.getStartAt());

        return toTransactionHistoryDTOList(transactionHistoryList);
    }

    private String generateFintechUserCode() {
        UUID uuid = UUID.randomUUID();
        String withoutHyphens = uuid.toString().replace("-", "");

        int length = withoutHyphens.length();
        if (length >= 12) {
            return withoutHyphens.substring(length - 12);
        } else {
            return withoutHyphens;
        }
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

    private User getLoginUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private List<TransactionHistoryDTO> toTransactionHistoryDTOList(List<TransactionHistory> transactionHistoryList) {
        return transactionHistoryList.stream()
                .map(this::toTransactionHistoryDTO)
                .collect(Collectors.toList());
    }

    private TransactionHistoryDTO toTransactionHistoryDTO(TransactionHistory transactionHistory) {
        TransactionHistoryDTO.TransactionHistoryDTOBuilder builder = TransactionHistoryDTO.builder()
                .transactionType(transactionHistory.getTransactionType())
                .transactionAt(transactionHistory.getCreatedAt())
                .amount(transactionHistory.getAmount())
                .balanceAfter(transactionHistory.getBalanceAfter())
                .content(transactionHistory.getContent());

        if (transactionHistory.getCounterAccount() != null) {
            builder.counterAccountName(transactionHistory.getCounterAccount().getUser().getName());
        }

        return builder.build();
    }

}
