package com.yongy.dotori.domain.chatGPT.dto;

import com.yongy.dotori.domain.plan.dto.CategoryDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDataDTO {
    private List<String> categoryGroups;
    private List<CategoryDTO> categorise;

    @Override
    public String toString() {
        return "카테고리그룹=" + categoryGroups +
                ", 카테고리=" + categorise;
    }
}
