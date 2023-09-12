package com.bank.podo.domain.account.entity;

import com.bank.podo.domain.account.enums.AccountType;
import com.bank.podo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Column(nullable = false)
    private String accountNumber;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(nullable = false)
    @ColumnDefault("0")
    private BigDecimal balance;

    @Column
    private LocalDateTime createAt;

    // 만기일
    @Column
    private LocalDateTime maturityAt;

    @ManyToOne
    @JoinColumn(name = "interest_id")
    private InterestRate interestRate;

    @Column
    private String loanInfo;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private int passwordRetryCount;

    @Column
    private boolean locked;

    @Builder
    public Account(String accountNumber, User user, AccountType accountType, BigDecimal balance, LocalDateTime maturityAt, InterestRate interestRate, String loanInfo, String password, int passwordRetryCount, boolean locked) {
        this.accountNumber = accountNumber;
        this.user = user;
        this.accountType = accountType;
        this.balance = balance;
        this.createAt = LocalDateTime.now();
        this.maturityAt = maturityAt;
        this.interestRate = interestRate;
        this.loanInfo = loanInfo;
        this.password = password;
        this.passwordRetryCount = passwordRetryCount;
        this.locked = locked;
    }

    public Account update(Account account) {
        if(account.getAccountType() != null) {
            this.accountType = account.getAccountType();
        }
        if(account.getPassword() != null) {
            this.password = account.getPassword();
        }
        if(account.getPasswordRetryCount() != -1) {
            this.passwordRetryCount = account.getPasswordRetryCount();
        }
        if(account.locked != this.locked) {
            this.locked = account.locked;
        }
        if(account.getLoanInfo() != null) {
            this.loanInfo = account.getLoanInfo();
        }
        if(account.getInterestRate() != null) {
            this.interestRate = account.getInterestRate();
        }

        return this;
    }

    public void deposit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
    }

    public void lock() {
        this.locked = true;
    }

    public void unlock() {
        this.locked = false;
    }

    public Account increasePasswordRetryCount() {
        this.passwordRetryCount++;
        if (this.passwordRetryCount >= 3) {
            this.lock();
        }

        return this;
    }
}
