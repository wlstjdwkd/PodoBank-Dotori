package com.yongy.dotori.domain.userAuth.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAccountDto {
    private Long bankSeq;

    private String accountNumber;

    @Builder
    public UserAccountDto(Long bankSeq, String accountNumber) {
        this.bankSeq = bankSeq;
        this.accountNumber = accountNumber;
    }
}
