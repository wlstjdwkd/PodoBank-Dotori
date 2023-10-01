package com.yongy.dotorimainservice.domain.planDetail.service;


import com.yongy.dotorimainservice.domain.planDetail.dto.response.PlanDetailListResDto;

import java.util.List;

public interface PlanDetailService {
    public List<PlanDetailListResDto> getPlanDetail(Long planSeq);
}
