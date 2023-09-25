package com.bank.podo.domain.openbank.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountVerificationCode {

    @Id
    String accountNumber;

    String code;

    LocalDateTime sendAt;
}
