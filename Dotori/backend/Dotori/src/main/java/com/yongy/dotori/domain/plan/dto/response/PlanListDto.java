package com.yongy.dotori.domain.plan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PlanListDto {
    private Long planSeq;
    private String startAt;
    private String endAt;
}
