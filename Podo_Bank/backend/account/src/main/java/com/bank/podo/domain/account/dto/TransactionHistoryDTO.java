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
    private String businessCode;

    @Builder
<<<<<<<< HEAD:Dotori/backend/Dotori/src/main/java/com/yongy/dotori/domain/payment/dto/TransactionHistoryDTO.java
    public TransactionHistoryDTO(String transactionType, LocalDateTime transactionAt, BigDecimal amount, BigDecimal balanceAfter, String counterAccountName, String content) {
========
    public TransactionHistoryDTO(TransactionType transactionType, LocalDateTime transactionAt, BigDecimal amount,
                                 BigDecimal balanceAfter, String counterAccountName, String content, String businessCode) {
>>>>>>>> develop:Podo_Bank/backend/account/src/main/java/com/bank/podo/domain/account/dto/TransactionHistoryDTO.java
        this.transactionType = transactionType;
        this.transactionAt = transactionAt;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.counterAccountName = counterAccountName;
        this.content = content;
        this.businessCode = businessCode;
    }
}