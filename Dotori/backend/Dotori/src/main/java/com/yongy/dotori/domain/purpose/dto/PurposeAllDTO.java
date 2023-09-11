package com.yongy.dotori.domain.purpose.dto;


import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurposeAllDTO {
    private BigDecimal currentBalance;
    private List<PurposeSummaryDTO> purposeList;
}
