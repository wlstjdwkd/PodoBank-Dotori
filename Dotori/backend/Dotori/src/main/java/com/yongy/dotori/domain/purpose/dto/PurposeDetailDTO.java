package com.yongy.dotori.domain.purpose.dto;


import com.yongy.dotori.domain.purposeData.entity.PurposeData;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurposeDetailDTO {
    private String purposeTitle;
    private BigDecimal currentBalance;
    private BigDecimal goalAmount;
    private List<PurposeData> purposeDataList;

//    @Builder
//    public PurposeDetailDTO(String purposeTitle, BigDecimal currentBalance, BigDecimal goalAmount, List<PurposeData> purposeDataList) {
//        this.purposeTitle = purposeTitle;
//        this.currentBalance = currentBalance;
//        this.goalAmount = goalAmount;
//        this.purposeDataList = purposeDataList;
//    }
}
