package com.yongy.dotori.domain.payment.dto.request;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentPodoReqDto {
    private String startAt;
    private Long accountSeq;
    private Long planSeq;

    @Builder
    public PaymentPodoReqDto(String startAt, Long accountSeq, Long planSeq) {
        this.startAt = startAt;
        this.accountSeq = accountSeq;
        this.planSeq = planSeq;
    }
}
