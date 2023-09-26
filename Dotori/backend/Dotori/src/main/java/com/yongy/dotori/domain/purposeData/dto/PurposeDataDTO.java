package com.yongy.dotori.domain.purposeData.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurposeDataDTO {
    private String dataName;
    private BigDecimal dataAmount;
    private BigDecimal dataCurrentBalance;
    private LocalDateTime dataCreatedAt;

}
