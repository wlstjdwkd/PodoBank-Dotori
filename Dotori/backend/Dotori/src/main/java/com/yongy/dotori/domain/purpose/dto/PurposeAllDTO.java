package com.yongy.dotori.domain.purpose.dto;


import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurposeAllDTO {
    private BigDecimal currentTotalSavings;
    private List<PurposeListDTO> purposeList;
}
