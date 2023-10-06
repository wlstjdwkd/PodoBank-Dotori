package com.yongy.dotori.domain.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@Builder
public class ActivePlanDetailDTO {
    private String title;
    private String groupTitle;
    private BigDecimal currentBalance;
    private BigDecimal goalAmount;
}
