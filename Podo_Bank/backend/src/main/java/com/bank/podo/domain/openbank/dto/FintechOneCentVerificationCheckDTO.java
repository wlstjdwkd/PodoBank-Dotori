package com.bank.podo.domain.openbank.dto;

import lombok.Data;

@Data
public class FintechOneCentVerificationCheckDTO {
    private String fintechServiceCode;
    private String account;
    private String verificationCode;
}
