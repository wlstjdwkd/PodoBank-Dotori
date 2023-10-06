package com.yongy.dotori.domain.plan.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yongy.dotori.domain.plan.dto.ActivePlanDTO;
import com.yongy.dotori.domain.plan.dto.PlanDTO;
import com.yongy.dotori.domain.plan.dto.PlanStateDTO;
import com.yongy.dotori.domain.plan.dto.SavingDTO;
import com.yongy.dotori.domain.plan.dto.response.PlanListDto;
import com.yongy.dotori.domain.plan.entity.State;

import java.util.List;

import java.util.List;

public interface PlanService {
    void createPlan(PlanDTO planDTO);
    void terminatePlan(Long planSeq);
    void updateState(State state, PlanStateDTO planStateDTO);
    void saving(SavingDTO savingDTO) throws JsonProcessingException;
    List<PlanListDto> getPlanList(Long userSeq);
    ActivePlanDTO findAllPlan(Long accountSeq) throws JsonProcessingException;
}
