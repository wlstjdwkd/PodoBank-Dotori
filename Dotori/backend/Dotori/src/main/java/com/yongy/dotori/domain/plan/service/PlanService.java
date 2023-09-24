package com.yongy.dotori.domain.plan.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yongy.dotori.domain.plan.dto.ActivePlanDTO;
import com.yongy.dotori.domain.plan.dto.PlanDTO;
import com.yongy.dotori.domain.plan.dto.PlanStateDTO;
import com.yongy.dotori.domain.plan.dto.SavingDTO;

import java.util.List;

public interface PlanService {
    void createPlan(PlanDTO planDTO);
    void terminatePlan(Long planSeq);
    void updateState(PlanStateDTO planStateDTO);
    void saving(SavingDTO savingDTO) throws JsonProcessingException;
    ActivePlanDTO findAllPlan(Long accountSeq) throws JsonProcessingException;
}
