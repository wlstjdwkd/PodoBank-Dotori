package com.yongy.dotori.domain.chatGPT.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

@Builder
@AllArgsConstructor
public class UnclassifiedResponseDTO {
    Long planDetailSeq;
    List<Long> paymentSeqs;
}
