package com.yongy.dotori.domain.userAuth.dto.request;

import lombok.*;

@NoArgsConstructor
@Data
public class UserAccountDto {
    private Long bankSeq;
    private String accountNumber;

}
