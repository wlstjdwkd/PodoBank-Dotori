package com.yongy.dotori.domain.payment.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PaymentPodoResDto {
    private String transactionAt; // 시간
    private BigDecimal amount; // 출금금액
    private BigDecimal balanceAfter; // 잔액
    private String content; // 사업자이름


}
