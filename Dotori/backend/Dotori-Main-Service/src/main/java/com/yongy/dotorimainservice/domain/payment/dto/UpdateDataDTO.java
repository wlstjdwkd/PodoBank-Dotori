package com.yongy.dotorimainservice.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UpdateDataDTO {
    Long planDetailSeq;
    Long paymentSeq;
}
