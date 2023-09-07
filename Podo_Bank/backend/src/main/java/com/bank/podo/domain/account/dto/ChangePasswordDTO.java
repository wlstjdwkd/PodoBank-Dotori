package com.bank.podo.domain.account.dto;

import lombok.Data;

@Data
public class ChangePasswordDTO {
    private Long accountNumber;
    private String oldPassword;
    private String newPassword;
}
