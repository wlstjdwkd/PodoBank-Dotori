package com.bank.podo.domain.user.dto;

import com.bank.podo.domain.user.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {
    private String name;
    private LocalDate birthdate;
    private Gender gender;
    private String contactInfo;
    private String address;
    private String id;
    private String password;
}
