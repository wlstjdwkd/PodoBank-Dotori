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

    @Column(name="user_seq")
    private Long userSeq;

    @Column(name="planDetail_seq")
    private Long planDetailSeq;

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

    public Payment updatePlanDetail(Long planDetailSeq){
        this.planDetailSeq = planDetailSeq;
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