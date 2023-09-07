package com.bank.podo.domain.account.dto;

import lombok.Data;

@Data
public class ResetPasswordDTO {
    private Long accountNumber;
    private String newPassword;
}
