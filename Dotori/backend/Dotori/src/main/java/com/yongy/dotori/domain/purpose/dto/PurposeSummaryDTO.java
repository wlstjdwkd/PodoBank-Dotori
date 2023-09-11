package com.yongy.dotori.domain.purpose.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public class PurposeSummaryDTO {
    private String title;
    private BigDecimal currentBalance;
    private BigDecimal goalAmount;
}
