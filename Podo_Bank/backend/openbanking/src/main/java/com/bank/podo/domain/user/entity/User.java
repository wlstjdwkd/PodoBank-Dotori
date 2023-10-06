package com.bank.podo.domain.user.entity;

<<<<<<<< HEAD:Podo_Bank/backend/auth/src/main/java/com/bank/podo/domain/user/entity/User.java
import com.bank.podo.domain.user.enums.Role;
import com.bank.podo.global.others.BaseEntity;
========
import com.bank.podo.global.others.entity.BaseEntity;
>>>>>>>> develop:Podo_Bank/backend/openbanking/src/main/java/com/bank/podo/domain/user/entity/User.java
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column
    private String role;

    // 이름
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate birthdate;

    @Column(nullable = false)
    private String phoneNumber;

    // 로그인 아이디
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    // 회원 탈퇴 시간
    @Column
    private LocalDateTime expiredAt;
}

