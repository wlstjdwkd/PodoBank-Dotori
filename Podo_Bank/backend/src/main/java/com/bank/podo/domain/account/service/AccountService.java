package com.bank.podo.domain.account.service;

import com.bank.podo.domain.account.dto.*;
import com.bank.podo.domain.account.exception.*;
import com.bank.podo.domain.account.repository.AccountRepository;
import com.bank.podo.domain.account.repository.TransactionHistoryRepository;
import com.bank.podo.domain.user.entity.User;
import com.bank.podo.domain.account.entity.Account;
import com.bank.podo.domain.account.entity.TransactionHistory;
import com.bank.podo.domain.account.entity.TransactionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final PlatformTransactionManager transactionManager;

    private final AccountRepository accountRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;

    public void createAccount(CreateAccountDTO createAccountDTO, PasswordEncoder passwordEncoder) {
        User user = getLoginUser();

        checkAccountPasswordFormat(createAccountDTO.getPassword());

        // TODO: 이지율 설정
        Account account = Account.builder()
                .accountNumber(generateAccountNumber())
                .user(user)
                .accountType(createAccountDTO.getAccountType())
                .balance(BigDecimal.ZERO)
                .password(passwordEncoder.encode(createAccountDTO.getPassword()))
                .build();
        accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public List<AccountDTO> getAccountList() {
        User user = getLoginUser();
        List<Account> accountList = accountRepository.findAllByUser(user);
        return toAccountDTOList(accountList);
    }

    @Transactional(readOnly = true)
    public AccountDTO getAccount(Long accountNumber) {
        Account account = accountRepository.findByAccountNumberAndMaturityAtIsNull(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("계좌를 찾을 수 없습니다."));
        return toAccountDTO(account);
    }

    @Transactional(readOnly = true)
    public List<TransactionHistoryDTO> getAccountHistory(Long accountNumber) {
        Account account = accountRepository.findByAccountNumberAndMaturityAtIsNull(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("계좌를 찾을 수 없습니다."));

        if(!account.getUser().getUserId().equals(getLoginUser().getUserId())) {
            throw new AccountUserNotMatchException("계좌의 소유자가 아닙니다.");
        }

        List<TransactionHistory> transactionHistoryList = transactionHistoryRepository.findAllByAccount(account);

        return toTransactionHistoryDTOList(transactionHistoryList);
    }

    @Transactional(noRollbackFor = PasswordRetryCountExceededException.class)
    public void changePassword(ChangePasswordDTO changePasswordDTO, PasswordEncoder passwordEncoder) {
        User user = getLoginUser();
        Account account = accountRepository.findByAccountNumberAndMaturityAtIsNull(changePasswordDTO.getAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("계좌를 찾을 수 없습니다."));

        checkAccountUserAndPassword(account, user, changePasswordDTO.getOldPassword(), passwordEncoder);

        checkAccountPasswordFormat(changePasswordDTO.getNewPassword());

        // Start a transaction
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = transactionManager.getTransaction(txDef);

        try {
            account.unlock();
            accountRepository.save(account.update(Account.builder()
                    .password(passwordEncoder.encode(changePasswordDTO.getNewPassword()))
                    .passwordRetryCount(0)
                    .build())); // Persist the updated password

            transactionManager.commit(txStatus); // Commit the transaction
        } catch (Exception e) {
            transactionManager.rollback(txStatus); // Rollback if an exception occurs
            throw e; // Rethrow the exception
        }
    }

    @Transactional
    public void resetPassword(ResetPasswordDTO resetPasswordDTO, PasswordEncoder passwordEncoder) {
        User user = getLoginUser();
        Account account = accountRepository.findByAccountNumberAndMaturityAtIsNull(resetPasswordDTO.getAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("계좌를 찾을 수 없습니다."));

        if(!account.getUser().getUserId().equals(user.getUserId())) {
            throw new AccountUserNotMatchException("계좌의 소유자가 아닙니다.");
        }

        checkAccountPasswordFormat(resetPasswordDTO.getNewPassword());

        // Start a transaction
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = transactionManager.getTransaction(txDef);

        try {
            account.unlock();
            accountRepository.save(account.update(Account.builder()
                    .password(passwordEncoder.encode(resetPasswordDTO.getNewPassword()))
                    .passwordRetryCount(0)
                    .build())); // Persist the updated password

            transactionManager.commit(txStatus); // Commit the transaction
        } catch (Exception e) {
            transactionManager.rollback(txStatus); // Rollback if an exception occurs
            throw e; // Rethrow the exception
        }
    }

    @Transactional(noRollbackFor = PasswordRetryCountExceededException.class)
    public void deposit(DepositDTO depositDTO, PasswordEncoder passwordEncoder) {
        User user = getLoginUser();
        log.info("Account number: {}", depositDTO.getAccountNumber());
        Account account = accountRepository.findByAccountNumberAndMaturityAtIsNull(depositDTO.getAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("계좌를 찾을 수 없습니다."));

        checkAccountUserAndPassword(account, user, depositDTO.getPassword(), passwordEncoder);

        Account account2 = accountRepository.findByAccountNumberAndMaturityAtIsNull(depositDTO.getAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("계좌를 찾을 수 없습니다."));
        log.info("Account: {}", account2.toString());

        BigDecimal depositAmount = depositDTO.getAmount();

        // Start a transaction
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = transactionManager.getTransaction(txDef);

        try {
            account.deposit(depositAmount); // Update balance in memory

            accountRepository.save(account); // Persist the updated balance

            // Create a transaction history record
            TransactionHistory depositHistory = TransactionHistory.builder()
                    .transactionType(TransactionType.DEPOSIT)
                    .amount(depositAmount)
                    .balanceAfter(account.getBalance())
                    .account(account)
                    .build();
            transactionHistoryRepository.save(depositHistory);

            transactionManager.commit(txStatus); // Commit the transaction
        } catch (Exception e) {
            transactionManager.rollback(txStatus); // Rollback if an exception occurs
            throw e; // Rethrow the exception
        }
    }


    @Transactional(noRollbackFor = PasswordRetryCountExceededException.class)
    public void withdraw(WithdrawDTO withdrawDTO, PasswordEncoder passwordEncoder) {
        User user = getLoginUser();
        Account account = accountRepository.findByAccountNumberAndMaturityAtIsNull(withdrawDTO.getAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("계좌를 찾을 수 없습니다."));

        checkAccountUserAndPassword(account, user, withdrawDTO.getPassword(), passwordEncoder);

        BigDecimal withdrawalAmount = withdrawDTO.getAmount();

        if(account.getBalance().compareTo(withdrawalAmount) < 0) {
            throw new InsufficientBalanceException("잔액이 부족합니다.");
        }

        // Start a transaction
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = transactionManager.getTransaction(txDef);

        try {
            account.withdraw(withdrawalAmount); // Update balance in memory

            accountRepository.save(account); // Persist the updated balance

            // Create a transaction history record
            TransactionHistory withdrawalHistory = TransactionHistory.builder()
                    .transactionType(TransactionType.WITHDRAWAL)
                    .amount(withdrawalAmount)
                    .balanceAfter(account.getBalance())
                    .account(account)
                    .build();
            transactionHistoryRepository.save(withdrawalHistory);

            transactionManager.commit(txStatus); // Commit the transaction
        } catch (Exception e) {
            transactionManager.rollback(txStatus); // Rollback if an exception occurs
            throw e; // Rethrow the exception
        }
    }

    @Transactional(noRollbackFor = PasswordRetryCountExceededException.class)
    public void transfer(TransferDTO transferDTO, PasswordEncoder passwordEncoder) {
        User user = getLoginUser();
        Account fromAccount = accountRepository.findByAccountNumberAndMaturityAtIsNull(transferDTO.getFromAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("계좌를 찾을 수 없습니다."));
        Account toAccount = accountRepository.findByAccountNumberAndMaturityAtIsNull(transferDTO.getToAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("계좌를 찾을 수 없습니다."));

        checkAccountUserAndPassword(fromAccount, user, transferDTO.getPassword(), passwordEncoder);

        BigDecimal transferAmount = transferDTO.getAmount();

        if(fromAccount.getBalance().compareTo(transferAmount) < 0) {
            throw new InsufficientBalanceException("잔액이 부족합니다.");
        }

        // Start a transaction
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = transactionManager.getTransaction(txDef);

        try {
            fromAccount.withdraw(transferAmount); // Update balance in memory
            toAccount.deposit(transferAmount); // Update balance in memory

            accountRepository.save(fromAccount); // Persist the updated balances
            accountRepository.save(toAccount);   // Persist the updated balances

            // Create transaction history records
            TransactionHistory fromAccountHistory = TransactionHistory.builder()
                    .transactionType(TransactionType.TRANSFER)
                    .amount(transferAmount.negate()) // Negative amount for withdrawal
                    .balanceAfter(fromAccount.getBalance())
                    .counterAccount(toAccount)
                    .account(fromAccount)
                    .build();
            TransactionHistory toAccountHistory = TransactionHistory.builder()
                    .transactionType(TransactionType.TRANSFER)
                    .amount(transferAmount) // Positive amount for deposit
                    .balanceAfter(toAccount.getBalance())
                    .counterAccount(fromAccount)
                    .account(toAccount)
                    .build();
            transactionHistoryRepository.save(fromAccountHistory);
            transactionHistoryRepository.save(toAccountHistory);

            transactionManager.commit(txStatus); // Commit the transaction
        } catch (Exception e) {
            transactionManager.rollback(txStatus); // Rollback if an exception occurs
            throw e; // Rethrow the exception
        }
    }

    public void deleteAccount() {
    }

    private User getLoginUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private void checkAccountUserAndPassword(Account account, User user, String password, PasswordEncoder passwordEncoder) {
        if(!account.getUser().getUserId().equals(user.getUserId())) {
            throw new AccountUserNotMatchException("계좌의 소유자가 아닙니다.");
        }

        if(account.isLocked()) {
            throw new AccountLockedException("계좌가 잠겨있습니다.");

        }

        if(account.getPasswordRetryCount() >= 3) {
            throw new PasswordRetryCountExceededException("비밀번호 재시도 횟수를 초과했습니다.");
        }

        if(!passwordEncoder.matches(password, account.getPassword())) {
            increasePasswordRetryCount(account);
            log.info(accountRepository.findByAccountNumberAndMaturityAtIsNull(account.getAccountNumber()).toString());
            throw new PasswordRetryCountExceededException("비밀번호가 일치하지 않습니다.");
        }
    }

    private void increasePasswordRetryCount(Account account) {
        account.increasePasswordRetryCount();
        log.info("Password retry count: {}", account.getPasswordRetryCount());
        if (account.getPasswordRetryCount() >= 3) {
            account.lock();
        }
        log.info("Account locked: {}", account.isLocked());
        accountRepository.save(account);
        log.info("Account saved: {}", accountRepository.findByAccountNumberAndMaturityAtIsNull(account.getAccountNumber()).toString());
    }


    private Long generateAccountNumber() {
        long prefix = 9775L;
        long randomSuffix = (long) (Math.random() * 9000000000000L) + 1000000000000L;
        long generatedNumber = prefix * 10000000000000L + randomSuffix;

        // 중복 체크
        while (accountRepository.existsByAccountNumber(generatedNumber)) {
            randomSuffix = (long) (Math.random() * 9000000000000L) + 1000000000000L;
            generatedNumber = prefix * 10000000000000L + randomSuffix;
        }

        return generatedNumber;
    }

    private void checkAccountPasswordFormat(String password) {
        String pattern = "^[0-9]{4}$";

        if(!Pattern.compile(pattern).matcher(password).matches()) {
            throw new AccountPasswordFormatException("계좌 비밀번호는 숫자 4자리여야 합니다.");
        }
    }

    private List<AccountDTO> toAccountDTOList(List<Account> accountList) {
        return accountList.stream()
                .map(this::toAccountDTO)
                .collect(Collectors.toList());
    }

    private AccountDTO toAccountDTO(Account account) {
        return AccountDTO.builder()
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .balance(account.getBalance().toString())
                .createAt(account.getCreateAt().toString())
                .interestRate(account.getInterestRate())
                .build();
    }

    private List<TransactionHistoryDTO> toTransactionHistoryDTOList(List<TransactionHistory> transactionHistoryList) {
        return transactionHistoryList.stream()
                .map(this::toTransactionHistoryDTO)
                .collect(Collectors.toList());
    }

    private TransactionHistoryDTO toTransactionHistoryDTO(TransactionHistory transactionHistory) {
        return TransactionHistoryDTO.builder()
                .transactionType(transactionHistory.getTransactionType())
                .transactionAt(transactionHistory.getTransactionAt())
                .amount(transactionHistory.getAmount())
                .balanceAfter(transactionHistory.getBalanceAfter())
                .counterName(transactionHistory.getCounterAccount().getUser().getName())
                .build();
    }
}
