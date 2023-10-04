package com.yongy.dotorimainservice.domain.payment.dto;

import com.yongy.dotorimainservice.domain.payment.dto.UpdateDataDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class UnclassifiedDTO {
    List<UpdateDataDTO> updateData;
}
