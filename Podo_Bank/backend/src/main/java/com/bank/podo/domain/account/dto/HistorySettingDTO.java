package com.bank.podo.domain.account.dto;

import lombok.Data;

@Data
public class HistorySettingDTO {
    private int searchMonth;    // 조회 기간 ( 단위 : 월 )
    private String transactionType;    // 거래 유형 ( WITHDRAW, DEPOSIT, ALL )
    private int sortType;   // 정렬 유형 ( 0 : 최신순, 1 : 오래된순 )
    private int page;   // 페이지 번호
}
