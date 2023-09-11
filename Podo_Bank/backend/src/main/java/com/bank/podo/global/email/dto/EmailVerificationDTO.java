package com.bank.podo.global.email.dto;

import lombok.Data;

@Data
public class EmailVerificationDTO {
    private String email;
    private String type;
}
