package com.yongy.dotoriuserservice.domain.account.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@Entity(name="accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="account_seq")
    private Long accountSeq;

    private Long userSeq;

    private Long bankSeq;

    private String accountNumber;

    private String accountTitle;

    @Column(name="fintech_code")
    private String fintechCode;

    private LocalDateTime deleteAt;

    @Builder
    public Account(Long accountSeq, Long userSeq, Long bankSeq, String accountNumber, String accountTitle, String fintechCode, LocalDateTime deleteAt) {
        this.accountSeq = accountSeq;
        this.userSeq = userSeq;
        this.bankSeq = bankSeq;
        this.accountNumber = accountNumber;
        this.accountTitle = accountTitle;
        this.fintechCode = fintechCode;
        this.deleteAt = deleteAt;
    }
}
