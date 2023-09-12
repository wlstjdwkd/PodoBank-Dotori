package com.bank.podo.global.email.dto;

import lombok.Data;

@Data
public class EmailVerificationCheckDTO {
    private String email;
    private String code;
}
