package com.yongy.dotori.domain.chatGPT.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CategoryDataDTO {
    private List<String> categoryGroup;
    private List<String> category;

    @Override
    public String toString() {
        return "카테고리그룹=" + categoryGroup +
                ", 카테고리=" + category;
    }
}
