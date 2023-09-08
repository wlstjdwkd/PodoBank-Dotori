package com.bank.podo.domain.account.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class TransferDTO {
    private Long senderAccountNumber;
    private Long receiverAccountNumber;
    private String password;
    private BigDecimal amount;
    private String senderContent;
    private String receiverContent;
}
