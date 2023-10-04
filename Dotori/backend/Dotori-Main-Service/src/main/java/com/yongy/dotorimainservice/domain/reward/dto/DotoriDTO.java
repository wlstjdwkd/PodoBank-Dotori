package com.yongy.dotorimainservice.domain.reward.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DotoriDTO {
    Long dotori;
    Long coin;
}
