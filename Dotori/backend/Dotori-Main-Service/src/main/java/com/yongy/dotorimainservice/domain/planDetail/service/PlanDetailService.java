package com.yongy.dotorimainservice.domain.planDetail.service;


import com.yongy.dotorimainservice.domain.planDetail.dto.response.PlanDetailDataDTO;
import com.yongy.dotorimainservice.domain.planDetail.dto.response.PlanDetailListResDto;
import com.yongy.dotorimainservice.domain.planDetail.dto.response.SpecificationDTO;

import java.util.List;

public interface PlanDetailService {
    SpecificationDTO getPlanDetail(Long planSeq);
    PlanDetailDataDTO findActiveCategoryDetail(Long planDetailSeq);
}
