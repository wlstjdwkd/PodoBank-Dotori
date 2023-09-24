package com.yongy.dotori.domain.chatGPT.dto;

import com.yongy.dotori.domain.plan.dto.ActiveCategoryDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDataDTO {
    private List<String> categoryGroups;
    private List<ActiveCategoryDTO> categorise;

    @Override
    public String toString() {
        return "categoryGroup=" + categoryGroups +
                ", categories=" + categorise;
    }
}
