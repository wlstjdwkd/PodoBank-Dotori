package com.yongy.dotori.domain.account.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class BodyDataDTO {
    String accountNumber;
    BigDecimal balance;
}
