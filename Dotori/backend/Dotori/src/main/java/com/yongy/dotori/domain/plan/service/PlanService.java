package com.yongy.dotori.domain.plan.service;

import com.yongy.dotori.domain.plan.dto.ActivePlanDTO;
import com.yongy.dotori.domain.plan.dto.PlanDTO;

import java.util.List;

public interface PlanService {
    void createPlan(PlanDTO planDTO);
    void terminatePlan(Long planSeq);
    ActivePlanDTO findAllPlan(Long accountSeq);
}
