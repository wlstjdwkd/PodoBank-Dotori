package com.yongy.dotori.domain.payment.entity;

import com.yongy.dotori.domain.planDetail.entity.PlanDetail;
import com.yongy.dotori.domain.user.entity.User;
//import jakarta.persistence.Entity;
//import jakarta.persistence.*;
//import jakarta.persistence.GenerationType;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity(name = "payments")
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_seq")
    private Long paymentSeq;

    @ManyToOne
    @JoinColumn(name = "userSeq")
    private User user;

    @ManyToOne
    @JoinColumn(name = "planDetailSeq")
    private PlanDetail planDetail;

    @Column(name = "payment_name", nullable = false)
    private String paymentName;

    @Column(name = "payment_price", nullable = false)
    private BigDecimal paymentPrice;

    @Builder
    public Payment(Long paymentSeq, User user, PlanDetail planDetail, String paymentName, BigDecimal paymentPrice) {
        this.paymentSeq = paymentSeq;
        this.user = user;
        this.planDetail = planDetail;
        this.paymentName = paymentName;
        this.paymentPrice = paymentPrice;
    }
}
