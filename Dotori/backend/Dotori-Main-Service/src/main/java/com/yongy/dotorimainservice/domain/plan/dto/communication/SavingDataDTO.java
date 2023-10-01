package com.yongy.dotorimainservice.domain.plan.dto.communication;

import com.yongy.dotorimainservice.domain.plan.dto.SavingDTO;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SavingDataDTO {
    SavingDTO savingDTO;
    Long accountSeq;
}
