package com.yongy.dotori.domain.plan.dto;

import com.yongy.dotori.domain.plan.entity.State;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class ActivePlanDTO {
    private BigDecimal accountBalance;
    private LocalDateTime startedAt;
    private LocalDateTime endAt;
    private State state;
    private Long planSeq;
    private Long unclassified; // 미분류 개수
    private List<ActivePlanDetailDTO> activePlanList;
}
