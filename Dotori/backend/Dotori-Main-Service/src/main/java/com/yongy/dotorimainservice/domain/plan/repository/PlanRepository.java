package com.yongy.dotorimainservice.domain.plan.repository;

import com.yongy.dotori.domain.plan.entity.Plan;
import com.yongy.dotori.domain.plan.entity.State;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    Plan findByPlanSeq(Long planSeq);
    Plan findByAccountAccountSeq(Long accountSeq);
    List<Plan> findAllByUserUserSeqAndPlanState(@Param("userSeq") Long userSeq, @Param("planState") State state);

    List<Plan> findAllByEndAt(LocalDateTime endAt);
    List<Plan> findAllByPlanStateAndTerminatedAtIsNull(State state);
    List<Plan> findAllByUserUserSeqAndTerminatedAtIsNull(Long userSeq);

}
