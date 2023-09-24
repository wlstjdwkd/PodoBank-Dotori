package com.yongy.dotori.domain.planDetail.repository;

import com.yongy.dotori.domain.planDetail.entity.PlanDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanDetailRepository extends JpaRepository<PlanDetail, Long> {
    PlanDetail findByPlanPlanSeq(Long planSeq);

}
