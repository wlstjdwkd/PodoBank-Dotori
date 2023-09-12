package com.yongy.dotori.domain.user.dto.request;


import com.yongy.dotori.domain.user.entity.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserInfoDto {
    private Role role;
    private String id;
    private String password;
    private String userName;
    private String birthDate;
    private String phoneNumber;

    @Builder
    public UserInfoDto(Role role, String id, String password, String userName, String birthDate, String phoneNumber) {
        this.role = role;
        this.id = id;
        this.password = password;
        this.userName = userName;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
    }
}
