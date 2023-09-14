package com.bank.podo.domain.openbank.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class FintechUserBalanceDTO {
    private String accountNumber;
    private BigDecimal balance;
}
