package src.main.java.com.bank.podo.domain.account.dto;

import src.main.java.com.bank.podo.domain.account.enums.TransactionType;
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
    private String counterAccountName;
    private String content;

    @Builder
    public TransactionHistoryDTO(TransactionType transactionType, LocalDateTime transactionAt, BigDecimal amount, BigDecimal balanceAfter, String counterAccountName, String content) {
        this.transactionType = transactionType;
        this.transactionAt = transactionAt;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.counterAccountName = counterAccountName;
        this.content = content;
    }
}
