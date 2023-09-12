package com.yongy.dotori.domain.purpose.dto;


import com.yongy.dotori.domain.purposeData.entity.PurposeData;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurposeDetailDTO {
    private String purposeTitle;
    private BigDecimal currentBalance;
    private BigDecimal goalAmount;
    private List<PurposeData> purposeDataList;
}
