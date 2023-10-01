package com.yongy.dotorimainservice.domain.plan.dto;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class SavingDTO {
    Long planSeq;
    List<PurposeSavingDTO> purposeSavingList;
    BigDecimal totalSaving;
}
