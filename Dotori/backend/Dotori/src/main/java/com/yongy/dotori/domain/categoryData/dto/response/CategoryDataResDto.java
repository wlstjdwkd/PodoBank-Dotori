package com.yongy.dotori.domain.categoryData.dto.response;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class CategoryDataResDto {
    private String dataCode;
    private String dataName;
    private int count;
}
