package com.yongy.dotori.domain.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    String categoryName;
    BigDecimal targetAmount;

    @Override
    public String toString() {
        return "{" +
                "categoryName='" + categoryName + '\'' +
                ", targetAmount=" + targetAmount +
                '}';
    }
}
