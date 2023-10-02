package com.yongy.dotorimainservice.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUnclassifiedDTO {
    List<UpdateDataDTO> updateData;
}
