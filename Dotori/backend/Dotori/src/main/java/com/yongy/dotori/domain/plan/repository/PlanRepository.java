package com.yongy.dotori.domain.plan.repository;

import com.yongy.dotori.domain.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    Plan findByPlanSeq(Long planSeq);

}
