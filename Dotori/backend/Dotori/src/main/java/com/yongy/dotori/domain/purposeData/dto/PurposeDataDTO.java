package com.yongy.dotori.domain.purposeData.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class PurposeDataDTO {
    String dataName;
    BigDecimal dataAmount;
    BigDecimal dataCurrentBalance;
    LocalDateTime dataCreatedAt;

}
