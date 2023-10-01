package com.yongy.dotorimainservice.domain.payment.entity;

import com.yongy.dotorimainservice.domain.planDetail.entity.PlanDetail;
import com.yongy.dotorimainservice.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "payments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
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

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;

    @Column(name = "business_code", nullable = false)
    private String businessCode;

    @Column(name = "checked")
    private boolean checked;

    public Payment updatePlanDetail(PlanDetail planDetailSeq){
        this.planDetail = planDetailSeq;
        return this;
    }

    public Payment updateChecked() {
        this.checked = true;
        return this;
    }

    @Override
    public String toString() {
        return "{ paymentSeq=" + paymentSeq +
                ", paymentName='" + paymentName + "'}";
    }
}