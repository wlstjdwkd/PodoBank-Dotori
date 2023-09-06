package com.bank.podo.domain.account.dto;

import com.bank.podo.domain.account.entity.TransactionType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TransactionHistoryDTO {
    private TransactionType transactionType;
    private LocalDateTime transactionAt;
    private BigDecimal amount;
    private BigDecimal balanceAfter;
    private String counterName;

    @Builder
    public TransactionHistoryDTO(TransactionType transactionType, LocalDateTime transactionAt, BigDecimal amount, BigDecimal balanceAfter, String counterName) {
        this.transactionType = transactionType;
        this.transactionAt = transactionAt;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.counterName = counterName;
    }
}
