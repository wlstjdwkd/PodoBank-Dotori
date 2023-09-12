package com.bank.podo.domain.user.entity;

import com.bank.podo.domain.user.enums.Gender;
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

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private String contactInfo;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private LocalDateTime registrationDate;

    // 로그인 아이디
    @Column(nullable = false, unique = true)
    private String id;

    @Column(nullable = false)
    private String password;

    @Builder
    public User(String name, LocalDate birthdate, Gender gender, String contactInfo, String address, String id, String password) {
        this.name = name;
        this.role = Role.ROLE_USER;
        this.birthdate = birthdate;
        this.gender = gender;
        this.contactInfo = contactInfo;
        this.address = address;
        this.registrationDate = LocalDateTime.now();
        this.id = id;
        this.password = password;
    }

    public User update(User user) {
        if(user.getAddress() != null)
            this.address = user.getAddress();
        if(user.id != null)
            this.id = user.id;
        if(user.password != null)
            this.password = user.password;

        return this;
    }
}

