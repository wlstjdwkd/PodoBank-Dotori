package com.bank.podo.domain.account.dto;

import lombok.Data;

@Data
public class ChangePasswordDTO {
    private String accountNumber;
    private String oldPassword;
    private String newPassword;
}
