package com.bank.podo.domain.account.entity;

import com.bank.podo.global.others.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "transaction_history",
    indexes = {
            @Index(name = "idx_account_seq", columnList = "account_seq"),
            @Index(name = "idx_counter_account_seq", columnList = "counter_account_seq"),
            @Index(name = "idx_create_at", columnList = "created_at")
    }
)
public class TransactionHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @ManyToOne
    @JoinColumn(name = "account_seq")
    private Account account;

    @Column
    private String transactionType;

    @Column
    private BigDecimal amount;

    @Column
    private BigDecimal balanceAfter;

    @ManyToOne
    @JoinColumn(name = "counter_account_seq")
    private Account counterAccount;

    @Column
    private String content;
}