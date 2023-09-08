package com.bank.podo.domain.account.dto;

import com.bank.podo.domain.account.enums.AccountType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateAccountDTO {
    private AccountType accountType;
    private String password;
}
