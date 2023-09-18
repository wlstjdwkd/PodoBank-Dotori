package com.bank.podo.domain.account.dto;

import lombok.Data;

@Data
public class SearchTypeDTO {
    private int searchMonth;
    private String transactionType;
    private int sortType;
    private int page;
}
