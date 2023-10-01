package com.yongy.dotorimainservice.domain.plan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PlanListDto {
    private Long planSeq;
    private String startAt;
    private String endAt;
}
