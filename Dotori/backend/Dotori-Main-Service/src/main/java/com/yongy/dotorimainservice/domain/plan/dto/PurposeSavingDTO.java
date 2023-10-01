package com.yongy.dotorimainservice.domain.plan.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PurposeSavingDTO {
    Long purposeSeq;
    BigDecimal savingAmount;
}
