package com.yongy.dotori.domain.payment.entity;

import com.yongy.dotori.domain.planDetail.entity.PlanDetail;
import com.yongy.dotori.domain.user.entity.User;
//import jakarta.persistence.Entity;
//import jakarta.persistence.*;
//import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity(name = "payments")
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @Column(name = "checked")
    private boolean checked;
}