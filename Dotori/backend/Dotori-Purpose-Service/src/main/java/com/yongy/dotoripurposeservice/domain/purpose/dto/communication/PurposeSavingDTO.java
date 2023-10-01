package com.yongy.dotoripurposeservice.domain.purpose.dto.communication;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PurposeSavingDTO {
    Long purposeSeq;
    BigDecimal savingAmount;
}
