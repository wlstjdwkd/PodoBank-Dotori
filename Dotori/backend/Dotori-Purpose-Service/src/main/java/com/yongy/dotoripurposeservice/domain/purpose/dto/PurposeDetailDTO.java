package com.yongy.dotoripurposeservice.domain.purpose.dto;


import com.yongy.dotoripurposeservice.domain.purposeData.dto.PurposeDataDTO;
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
    private String purposeTitle;
    private BigDecimal currentBalance;
    private BigDecimal goalAmount;
    private LocalDate startedAt;
    private LocalDate endAt;
    private List<PurposeDataDTO> purposeDataList;
}
