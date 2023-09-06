package com.bank.podo.domain.user.dto;

import com.bank.podo.domain.user.entity.Gender;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class UserInfoDTO {
    private String name;
    private LocalDate birthdate;
    private Gender gender;
    private String contactInfo;
    private String address;
    private String id;

    @Builder
    public UserInfoDTO(String name, LocalDate birthdate, Gender gender, String contactInfo, String address, String id) {
        this.name = name;
        this.birthdate = birthdate;
        this.gender = gender;
        this.contactInfo = contactInfo;
        this.address = address;
        this.id = id;
    }

}
