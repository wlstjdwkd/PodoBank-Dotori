package com.yongy.dotori.domain.plan.service;

import com.yongy.dotori.domain.plan.repository.PlanRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService{

    private final PlanRepository planRepository;

    @Override
    public void createPlan() {


    }
}
