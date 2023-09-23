package com.yongy.dotori.domain.plan.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yongy.dotori.domain.plan.dto.PlanDTO;
import com.yongy.dotori.domain.plan.dto.PlanStateDTO;
import com.yongy.dotori.domain.plan.dto.SavingDTO;

public interface PlanService {
    void createPlan(PlanDTO planDTO);
    void terminatePlan(Long planSeq);
    void updateState(PlanStateDTO planStateDTO);
    void saving(SavingDTO savingDTO) throws JsonProcessingException;
}
