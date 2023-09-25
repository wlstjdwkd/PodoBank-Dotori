package com.yongy.dotori.domain.userAuth.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserAccountDto {
    private Long bankSeq;
    private String accountNumber;

}
