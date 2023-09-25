package com.yongy.dotori.domain.planDetail.repository;

import com.yongy.dotori.domain.planDetail.entity.PlanDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanDetailRepository extends JpaRepository<PlanDetail, Long> {
    List<PlanDetail> findAllByPlanPlanSeq(Long planSeq);
    PlanDetail findByPlanPlanSeq(Long planSeq);


}
