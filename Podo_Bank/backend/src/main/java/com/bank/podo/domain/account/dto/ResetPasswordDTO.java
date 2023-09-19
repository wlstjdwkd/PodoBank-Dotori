package com.bank.podo.domain.account.dto;

import lombok.Data;

@Data
public class ResetPasswordDTO {
    private String accountNumber;
    private String newPassword;
    private String successCode;
}
