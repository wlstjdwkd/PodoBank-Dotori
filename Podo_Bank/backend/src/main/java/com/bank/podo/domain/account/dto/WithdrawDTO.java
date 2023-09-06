package com.bank.podo.domain.account.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class WithdrawDTO {
    private Long accountNumber;
    private String password;
    private BigDecimal amount;
}
