package com.yongy.dotori.domain.purpose.dto;


import com.yongy.dotori.domain.purposeData.dto.PurposeDataDTO;
import com.yongy.dotori.domain.purposeData.entity.PurposeData;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurposeDetailDTO {
    String purposeTitle;
    BigDecimal currentBalance;
    BigDecimal goalAmount;
    LocalDate startedAt;
    LocalDate endAt;
    List<PurposeDataDTO> purposeDataList;
}
