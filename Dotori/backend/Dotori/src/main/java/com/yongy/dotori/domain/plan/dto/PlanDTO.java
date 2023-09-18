package com.yongy.dotori.domain.plan.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PlanDTO {
    private Long accountSeq;
    private LocalDateTime startedAt;
    private LocalDateTime endAt;
    private List<CategoryGroupListDTO> categoryGroupList;
}
