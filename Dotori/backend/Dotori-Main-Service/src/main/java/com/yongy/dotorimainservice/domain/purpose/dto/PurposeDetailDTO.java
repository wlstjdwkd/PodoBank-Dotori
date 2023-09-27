package com.yongy.dotorimainservice.domain.purpose.dto;


import com.yongy.dotori.domain.purposeData.dto.PurposeDataDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
