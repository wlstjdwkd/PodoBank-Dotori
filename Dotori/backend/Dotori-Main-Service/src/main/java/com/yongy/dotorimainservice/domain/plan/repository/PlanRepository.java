package com.yongy.dotorimainservice.domain.plan.repository;

import com.yongy.dotorimainservice.domain.account.entity.Account;
import com.yongy.dotorimainservice.domain.plan.entity.Plan;
import com.yongy.dotorimainservice.domain.plan.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    Plan findByPlanSeq(Long planSeq);
    Plan findByAccountAccountSeqAndPlanStateAndTerminatedAtIsNull(Long accountSeq, State state);
//    List<Plan> findAllByUserUserSeqAndPlanState(@Param("userSeq") Long userSeq, @Param("planState") State state);
//
//    List<Plan> findAllByEndAt(LocalDateTime endAt);

    List<Plan> findAllByPlanStateAndTerminatedAtIsNull(State state);

    List<Plan> findAllByUserSeqAndTerminatedAtIsNotNull(Long userSeq);
    List<Plan> findAllByUserSeqAndTerminatedAtIsNull(Long userSeq);

}
