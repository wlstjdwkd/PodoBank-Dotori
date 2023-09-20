package com.bank.podo.domain.account.service;

import com.bank.podo.domain.account.dto.*;
import com.bank.podo.domain.account.entity.Account;
import com.bank.podo.domain.account.entity.AccountCategory;
import com.bank.podo.domain.account.entity.TransactionHistory;
import com.bank.podo.domain.account.enums.TransactionType;
import com.bank.podo.domain.account.exception.*;
import com.bank.podo.domain.account.repository.AccountCategoryRepository;
import com.bank.podo.domain.account.repository.AccountRepository;
import com.bank.podo.domain.account.repository.TransactionHistoryRepository;
import com.bank.podo.domain.user.entity.User;
import com.bank.podo.global.email.entity.VerificationSuccess;
import com.bank.podo.global.email.enums.VerificationType;
import com.bank.podo.global.email.exception.EmailVerificationException;
import com.bank.podo.global.email.repository.VerificationSuccessRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountCategoryRepository accountCategoryRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;
    private final VerificationSuccessRepository verificationSuccessRepository;

    public List<AccountCategoryDTO> getAccountTypeList() {
        List<AccountCategory> accountCategories = accountCategoryRepository.findAll();
        return toAccountCategoryDTOList(accountCategories);
    }

    public AccountDTO createAccount(CreateAccountDTO createAccountDTO, PasswordEncoder passwordEncoder) {
        User user = getLoginUser();

        checkAccountPasswordFormat(createAccountDTO.getPassword());

        AccountCategory accountCategory = accountCategoryRepository.findById(createAccountDTO.getAccountCategoryId())
                .orElseThrow(() -> new AccountNotFoundException("계좌 종류를 찾을 수 없습니다."));

        Account account = Account.builder()
                .accountNumber(generateAccountNumber())
                .user(user)
                .accountType(accountCategory.getAccountType())
                .balance(BigDecimal.ZERO)
                .password(passwordEncoder.encode(createAccountDTO.getPassword()))
                .accountCategory(accountCategory)
                .nickname(user.getName()+"님의 "+accountCategory.getAccountName())
                .build();
        accountRepository.save(account);

        return AccountDTO.builder()
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .balance(account.getBalance().toString())
                .interestRate(account.getAccountCategory().getInterestRate())
                .createAt(account.getCreatedAt())
                .nickname(account.getName())
                .build();
    }

    @Transactional(readOnly = true)
    public List<AccountDTO> getAccountList() {
        User user = getLoginUser();
        List<Account> accountList = accountRepository.findAllByUserAndDeletedFalse(user);
        return toAccountDTOList(accountList);
    }

    @Transactional(readOnly = true)
    public String getAccountOwnerName(String accountNumber) {
        Account account = accountRepository.findByAccountNumberAndDeletedFalse(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("계좌를 찾을 수 없습니다."));
        return account.getUser().getName();
    }

    @Transactional(readOnly = true)
    public AccountDTO getAccountDetail(String accountNumber) {
        Account account = accountRepository.findByAccountNumberAndDeletedFalse(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("계좌를 찾을 수 없습니다."));
        return toAccountDTO(account);
    }

    @Transactional(readOnly = true)
    public List<TransactionHistoryDTO> getAccountHistory(String accountNumber, int searchMonth, String transactionType, int sortType, int page) {
        Account account = accountRepository.findByAccountNumberAndDeletedFalse(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("계좌를 찾을 수 없습니다."));

        if(!account.getUser().getUserId().equals(getLoginUser().getUserId())) {
            throw new AccountUserNotMatchException("계좌의 소유자가 아닙니다.");
        }

        // 페이지 번호와 정렬 유형에 따라 페이지 요청 생성
        PageRequest pageRequest = PageRequest.of(page, 10,
                sortType == 0 ? Sort.by("createdAt").descending() : Sort.by("createdAt").ascending());

        LocalDateTime startDate = LocalDateTime.now().minusDays(searchMonth);

        List<TransactionHistory> transactionHistoryList = switch (transactionType) {
            case "DEPOSIT" ->
                    transactionHistoryRepository.findAllByAccountAndTransactionTypeAndCreatedAtGreaterThanEqual(account, TransactionType.DEPOSIT, startDate, pageRequest);
            case "WITHDRAWAL" ->
                    transactionHistoryRepository.findAllByAccountAndTransactionTypeAndCreatedAtGreaterThanEqual(account, TransactionType.WITHDRAWAL, startDate, pageRequest);
            case "ALL" ->
                    transactionHistoryRepository.findAllByAccountAndCreatedAtGreaterThanEqual(account, startDate, pageRequest);
            default -> new ArrayList<>();
        };


        return toTransactionHistoryDTOList(transactionHistoryList);
    }

    @Transactional(noRollbackFor = PasswordRetryCountExceededException.class)
    public void changePassword(ChangeAccountPasswordDTO changePasswordDTO, PasswordEncoder passwordEncoder) {
        User user = getLoginUser();
        Account account = accountRepository.findByAccountNumberAndDeletedFalse(changePasswordDTO.getAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("계좌를 찾을 수 없습니다."));

        checkAccountUserAndPassword(account, user, changePasswordDTO.getOldPassword(), passwordEncoder);

        checkAccountPasswordFormat(changePasswordDTO.getNewPassword());

        account.unlock();
        accountRepository.save(account.update(Account.builder()
                .password(passwordEncoder.encode(changePasswordDTO.getNewPassword()))
                .build()));
    }

    @Transactional
    public void resetPassword(ResetAccountPasswordDTO resetPasswordDTO, PasswordEncoder passwordEncoder) {
        User user = getLoginUser();
        Account account = accountRepository.findByAccountNumberAndDeletedFalse(resetPasswordDTO.getAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("계좌를 찾을 수 없습니다."));

        if(!account.getUser().getUserId().equals(user.getUserId())) {
            throw new AccountUserNotMatchException("계좌의 소유자가 아닙니다.");
        }

        VerificationSuccess verificationSuccess = verificationSuccessRepository.findById(user.getEmail())
                .orElseThrow(() -> new EmailVerificationException("이메일 인증이 만료되었습니다."));

        if(!verificationSuccess.getSuccessCode().equals(resetPasswordDTO.getSuccessCode())
                || !verificationSuccess.getType().equals(VerificationType.RESET_ACCOUNT_PASSWORD)) {
            throw new EmailVerificationException("이메일 인증이 만료되었습니다.");
        }

        checkAccountPasswordFormat(resetPasswordDTO.getNewPassword());

        account.unlock();
        accountRepository.save(account.update(Account.builder()
                .password(passwordEncoder.encode(resetPasswordDTO.getNewPassword()))
                .build()));

    }

    @Transactional(noRollbackFor = PasswordRetryCountExceededException.class)
    public void deposit(DepositDTO depositDTO, PasswordEncoder passwordEncoder) {
        User user = getLoginUser();
        Account account = accountRepository.findByAccountNumberAndDeletedFalse(depositDTO.getAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("계좌를 찾을 수 없습니다."));

        checkAccountUserAndPassword(account, user, depositDTO.getPassword(), passwordEncoder);

        BigDecimal depositAmount = depositDTO.getAmount();

        account.deposit(depositAmount);

        accountRepository.save(account);

        TransactionHistory depositHistory = TransactionHistory.builder()
                .transactionType(TransactionType.DEPOSIT)
                .amount(depositAmount)
                .balanceAfter(account.getBalance())
                .account(account)
                .content(depositDTO.getContent())
                .build();
        transactionHistoryRepository.save(depositHistory);
    }


    @Transactional(noRollbackFor = PasswordRetryCountExceededException.class)
    public void withdraw(WithdrawDTO withdrawDTO, PasswordEncoder passwordEncoder) {
        User user = getLoginUser();
        Account account = accountRepository.findByAccountNumberAndDeletedFalse(withdrawDTO.getAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("계좌를 찾을 수 없습니다."));

        checkAccountUserAndPassword(account, user, withdrawDTO.getPassword(), passwordEncoder);

        BigDecimal withdrawalAmount = withdrawDTO.getAmount();

        if(account.getBalance().compareTo(withdrawalAmount) < 0) {
            throw new InsufficientBalanceException("잔액이 부족합니다.");
        }

        account.withdraw(withdrawalAmount);

        accountRepository.save(account);

        TransactionHistory withdrawalHistory = TransactionHistory.builder()
                .transactionType(TransactionType.WITHDRAWAL)
                .amount(withdrawalAmount)
                .balanceAfter(account.getBalance())
                .account(account)
                .content(withdrawDTO.getContent())
                .build();
        transactionHistoryRepository.save(withdrawalHistory);
    }

    @Transactional(noRollbackFor = PasswordRetryCountExceededException.class)
    public void transfer(TransferDTO transferDTO, PasswordEncoder passwordEncoder) {
        User user = getLoginUser();
        Account senderAccount = accountRepository.findByAccountNumberAndDeletedFalse(transferDTO.getSenderAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("계좌를 찾을 수 없습니다."));
        Account receiverAccount = accountRepository.findByAccountNumberAndDeletedFalse(transferDTO.getReceiverAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("계좌를 찾을 수 없습니다."));

        checkAccountUserAndPassword(senderAccount, user, transferDTO.getPassword(), passwordEncoder);

        BigDecimal transferAmount = transferDTO.getAmount();

        if(senderAccount.getBalance().compareTo(transferAmount) < 0) {
            throw new InsufficientBalanceException("잔액이 부족합니다.");
        }

        senderAccount.withdraw(transferAmount);
        receiverAccount.deposit(transferAmount);

        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);

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
        transactionHistoryRepository.save(senderAccountHistory);
        transactionHistoryRepository.save(receiverAccountHistory);
    }

    public void deleteAccount(DeleteAccountDTO deleteAccountDTO, PasswordEncoder passwordEncoder) {
        User user = getLoginUser();
        Account account = accountRepository.findByAccountNumberAndDeletedFalse(deleteAccountDTO.getAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("계좌를 찾을 수 없습니다."));

        checkAccountUserAndPassword(account, user, deleteAccountDTO.getPassword(), passwordEncoder);

        if(account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            log.info(account.getBalance().toString());
            throw new AccountBalanceNotZeroException("잔액이 남아있습니다.");
        }

        accountRepository.save(account.delete());
    }

    @Transactional(readOnly = true)
    public List<RecentAccountDTO> getRecentTransferAccountList(String accountNumber) {
        User user = getLoginUser();
        Account account = accountRepository.findByAccountNumberAndDeletedFalse(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("계좌를 찾을 수 없습니다."));

        if(!account.getUser().getUserId().equals(user.getUserId())) {
            throw new AccountUserNotMatchException("계좌의 소유자가 아닙니다.");
        }

        List<Account> accountList = transactionHistoryRepository.findThreeMostRecentUniqueAccounts(account);

        return accountList.stream()
                .map(counterAccount -> RecentAccountDTO.builder()
                        .accountNumber(counterAccount.getAccountNumber())
                        .accountName(counterAccount.getUser().getName())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateAccountNickname(UpdateAccountNicknameDTO updateAccountNicknameDTO) {
        User user = getLoginUser();

        Account account = accountRepository.findByAccountNumberAndDeletedFalse(updateAccountNicknameDTO.getAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("계좌를 찾을 수 없습니다."));

        if(!account.getUser().getUserId().equals(user.getUserId())) {
            throw new AccountUserNotMatchException("계좌의 소유자가 아닙니다.");
        }

        account.update(Account.builder()
                .nickname(updateAccountNicknameDTO.getNickname())
                .build());

        accountRepository.save(account);
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

        if(!passwordEncoder.matches(password, account.getPassword())) {
            increasePasswordRetryCount(account);
            throw new PasswordRetryCountExceededException("비밀번호가 일치하지 않습니다.", account.getPasswordRetryCount());
        }

        // 비밀번호 일치 시, 비밀번호 재시도 횟수 초기화
        accountRepository.save(account.update(Account.builder()
                .passwordRetryCount(0)
                .build()));
    }

    private void increasePasswordRetryCount(Account account) {
        account.increasePasswordRetryCount();
        if (account.getPasswordRetryCount() >= 3) {
            account.lock();
        }
        accountRepository.save(account);
    }


    private String generateAccountNumber() {
        StringBuilder sb = new StringBuilder();

        sb.append("9775");

        Random random = new Random();
        for (int i = 0; i < 9; i++) {
            int digit = random.nextInt(10); // 0부터 9까지의 숫자 생성
            sb.append(digit);
        }

        // 중복 체크
        while (accountRepository.existsByAccountNumberAndDeletedFalse(sb.toString())) {
            sb = new StringBuilder();
            sb.append("9775");

            for (int i = 0; i < 9; i++) {
                int digit = random.nextInt(10); // 0부터 9까지의 숫자 생성
                sb.append(digit);
            }
        }

        return sb.toString();
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
                .interestRate(account.getAccountCategory().getInterestRate())
                .createAt(account.getCreatedAt())
                .nickname(account.getName())
                .build();
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

    private List<AccountCategoryDTO> toAccountCategoryDTOList(List<AccountCategory> accountCategories) {
        return accountCategories.stream()
                .map(this::toAccountCategoryDTO)
                .collect(Collectors.toList());
    }

    private AccountCategoryDTO toAccountCategoryDTO(AccountCategory accountCategory) {
        return AccountCategoryDTO.builder()
                .accountCategoryId(accountCategory.getAccountCategoryId())
                .accountType(accountCategory.getAccountType())
                .accountName(accountCategory.getAccountName())
                .accountDescription(accountCategory.getAccountDescription())
                .interestRate(accountCategory.getInterestRate())
                .build();
    }

}
