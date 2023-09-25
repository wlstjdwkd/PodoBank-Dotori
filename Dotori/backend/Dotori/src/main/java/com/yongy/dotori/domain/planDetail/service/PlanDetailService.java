package com.yongy.dotori.domain.planDetail.service;

import com.yongy.dotori.domain.planDetail.dto.response.PlanDetailListResDto;

import java.util.List;

public interface PlanDetailService {
    public List<PlanDetailListResDto> getPlanDetail(Long planSeq);
}
