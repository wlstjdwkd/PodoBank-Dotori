package com.yongy.dotori.domain.payment.repository;

import com.yongy.dotori.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findAllByPlanDetailPlanDetailSeq(Long planDetailSeq);
    Long countByPlanDetailPlanDetailSeq(Long planDetailSeq);
}
