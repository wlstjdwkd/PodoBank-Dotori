package com.bank.podo.domain.account.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class DepositDTO {
    private Long accountNumber;
    private BigDecimal amount;
    private String password;
}
