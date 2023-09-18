package com.yongy.dotori.domain.purpose.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurposeDTO {
    private String purposeTitle;
    private BigDecimal goalAmount;
    private LocalDateTime startedAt;
    private LocalDateTime endAt;
}
