package com.yongy.dotori.domain.categoryGroup.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryGroupDTO {
    private Long categoryGroupSeq;
    private String groupTitle;
}
