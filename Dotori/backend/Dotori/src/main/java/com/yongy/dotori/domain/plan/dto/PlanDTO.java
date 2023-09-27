package com.yongy.dotori.domain.plan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanDTO {
    private Long accountSeq;

    private String startedAt;

    private String endAt;

    private List<CategoryGroupListDTO> categoryGroupList;

    // startedAt을 문자열로 받아 LocalDateTime으로 변환하는 메서드 추가
//    public void setStartedAt(String startedAt) {
//        this.startedAt = LocalDateTime.parse(startedAt, DateTimeFormatter.ISO_DATE_TIME);
//    }
//
//    // endAt을 문자열로 받아 LocalDateTime으로 변환하는 메서드 추가
//    public void setEndAt(String endAt) {
//        this.endAt = LocalDateTime.parse(endAt, DateTimeFormatter.ISO_DATE_TIME);
//    }
}
