package com.bank.podo.domain.account.entity;

import com.bank.podo.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountNumber;

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
    public Account(Long accountNumber, User user, AccountType accountType, BigDecimal balance, LocalDateTime maturityAt, InterestRate interestRate, String loanInfo, String password, int passwordRetryCount, boolean locked) {
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

    public void increasePasswordRetryCount() {
        this.passwordRetryCount++;
    }
}
