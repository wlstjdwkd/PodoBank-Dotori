package com.bank.podo.domain.user.dto;

import lombok.Data;

@Data
public class ResetPasswordDTO {
    private String email;
    private String newPassword;
    private String successCode;
}
