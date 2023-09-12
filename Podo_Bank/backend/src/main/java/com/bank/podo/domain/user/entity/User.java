package com.bank.podo.domain.user.entity;

import com.bank.podo.domain.user.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Enumerated(EnumType.STRING)
    private Role role;

    // 이름
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate birthdate;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private LocalDateTime registrationDate;

    // 로그인 아이디
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Builder
    public User(String name, LocalDate birthdate, String phoneNumber, String email, String password) {
        this.name = name;
        this.birthdate = birthdate;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.registrationDate = LocalDateTime.now();
        this.role = Role.ROLE_USER;
    }

    public User update(User user) {
        if(user.password != null)
            this.password = user.password;

        return this;
    }
}

