package com.yongy.dotorimainservice.domain.planDetail.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Data
public class SpecificationDTO {
    BigDecimal additionalSaving;
    List<PlanDetailListResDto> planDetailList;
}
