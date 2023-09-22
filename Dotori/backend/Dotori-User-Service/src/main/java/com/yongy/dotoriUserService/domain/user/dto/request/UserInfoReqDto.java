package com.yongy.dotoriUserService.domain.user.dto.request;



import com.yongy.dotoriUserService.domain.user.entity.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoReqDto {
    private Role role;
    private String id;
    private String password;
    private String userName;
    private String birthDate;
    private String phoneNumber;

    @Builder
    public UserInfoReqDto(Role role, String id, String password, String userName, String birthDate, String phoneNumber) {
        this.role = role;
        this.id = id;
        this.password = password;
        this.userName = userName;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
    }
}
