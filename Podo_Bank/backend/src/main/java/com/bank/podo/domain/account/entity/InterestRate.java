package com.bank.podo.domain.account.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class InterestRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long interestId;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column
    private LocalDate validFrom;

    @Column
    private LocalDate validUntil;

    @Column
    private BigDecimal interestRate;

    @Builder
    public InterestRate(AccountType accountType, LocalDate validFrom, LocalDate validUntil, BigDecimal interestRate) {
        this.accountType = accountType;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
        this.interestRate = interestRate;
    }
}
