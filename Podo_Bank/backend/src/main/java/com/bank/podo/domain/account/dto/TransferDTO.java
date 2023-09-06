package com.bank.podo.domain.account.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class TransferDTO {
    private Long fromAccountNumber;
    private Long toAccountNumber;
    private String password;
    private BigDecimal amount;
}
