package com.bank.podo.domain.account.dto;

import com.bank.podo.domain.account.entity.AccountType;
import com.bank.podo.domain.account.entity.InterestRate;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountDTO {
    private String accountNumber;
    private AccountType accountType;
    private String balance;
    private String createAt;
    private InterestRate interestRate;

    @Builder
    public AccountDTO(String accountNumber, AccountType accountType, String balance, String createAt, InterestRate interestRate) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = balance;
        this.createAt = createAt;
        this.interestRate = interestRate;
    }
}
