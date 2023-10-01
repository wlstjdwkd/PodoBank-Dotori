package com.yongy.dotoriuserservice.domain.userAuth.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BankDto {
    private Long bankSeq;
    private String bankName;
    private String bankUrl;
    private String bankId;
    private String bankPwd;
    private String serviceCode;
}
