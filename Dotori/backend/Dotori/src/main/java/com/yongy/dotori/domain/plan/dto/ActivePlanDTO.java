package com.yongy.dotori.domain.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class ActivePlanDTO {
    private BigDecimal accountBalance;
    private LocalDateTime endAt;
    private Long unclassified; // 미분류 개수
    private List<ActivePlanDetailDTO> activePlanList;
}
