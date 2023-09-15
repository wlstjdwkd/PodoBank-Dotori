package com.bank.podo.domain.account.dto;

import com.bank.podo.domain.account.enums.AccountType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class AccountDTO {
    private String accountNumber;
    private AccountType accountType;
    private String balance;
    private String createAt;
    private BigDecimal interestRate;

    @Builder
    public AccountDTO(String accountNumber, AccountType accountType, String balance, String createAt, BigDecimal interestRate) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = balance;
        this.createAt = createAt;
        this.interestRate = interestRate;
    }
}
