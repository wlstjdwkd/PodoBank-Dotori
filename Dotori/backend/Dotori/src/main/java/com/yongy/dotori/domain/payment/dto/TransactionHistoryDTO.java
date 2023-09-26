package com.yongy.dotori.domain.payment.dto;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TransactionHistoryDTO {
    private String transactionType;
    private LocalDateTime transactionAt;
    private BigDecimal amount;
    private BigDecimal balanceAfter;
    private String counterAccountName;
    private String content;

    @Builder
    public TransactionHistoryDTO(String transactionType, LocalDateTime transactionAt, BigDecimal amount, BigDecimal balanceAfter, String counterAccountName, String content) {
        this.transactionType = transactionType;
        this.transactionAt = transactionAt;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.counterAccountName = counterAccountName;
        this.content = content;
    }
}