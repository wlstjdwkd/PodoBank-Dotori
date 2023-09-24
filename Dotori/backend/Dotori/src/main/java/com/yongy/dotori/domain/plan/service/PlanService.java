package com.yongy.dotori.domain.plan.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yongy.dotori.domain.plan.dto.PlanDTO;
import com.yongy.dotori.domain.plan.dto.SavingDTO;
import com.yongy.dotori.domain.plan.dto.response.PlanListDto;

import java.util.List;

public interface PlanService {
    void createPlan(PlanDTO planDTO);
    void terminatePlan(Long planSeq);
    void saving(SavingDTO savingDTO) throws JsonProcessingException;
    List<PlanListDto> getPlanList(Long userSeq);
}
