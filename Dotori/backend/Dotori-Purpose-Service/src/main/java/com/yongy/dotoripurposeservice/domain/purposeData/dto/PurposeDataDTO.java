package com.yongy.dotoripurposeservice.domain.purposeData.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
public class PurposeDataDTO {
    String dataName;
    BigDecimal dataAmount;
    BigDecimal dataCurrentBalance;
    LocalDateTime dataCreatedAt;
}
