package com.bank.podo.domain.account.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class TransactionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @ManyToOne
    @JoinColumn(name = "account_number")
    private Account account;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column
    private LocalDateTime transactionAt;

    @Column
    private BigDecimal amount;

    @Column
    private BigDecimal balanceAfter;

    @ManyToOne
    @JoinColumn(name = "counter_account_number")
    private Account counterAccount;

    @Column
    private String transactionDetails;

    @Builder
    public TransactionHistory(Account account, TransactionType transactionType, BigDecimal amount, BigDecimal balanceAfter, Account counterAccount, String transactionDetails) {
        this.account = account;
        this.transactionType = transactionType;
        this.transactionAt = LocalDateTime.now();
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.counterAccount = counterAccount;
        this.transactionDetails = transactionDetails;
    }
}