package com.yongy.dotori.domain.user.entity;

import com.yongy.dotori.domain.account.entity.Account;
import com.yongy.dotori.domain.plan.entity.Plan;
import com.yongy.dotori.domain.purpose.entity.Purpose;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Entity(name="users")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_seq")
    private Long userSeq;

    @Column(nullable = false)
    private String id;

    @Column(nullable = false)
    private String password;

    @Column(name="birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name="user_name", nullable = false)
    private String userName;

    @Column(name="phone_number", nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name="auth_provider", nullable = false)
    private Provider authProvider;

    @OneToMany(mappedBy = "user")
    private List<Purpose> purposeList;

    @OneToMany(mappedBy = "user")
    private List<Account> accountList;

    @OneToMany(mappedBy = "user")
    private List<Plan> planList;

    @Builder
    public User(Long userSeq, String id, String password, LocalDate birthDate, String userName, String phoneNumber, Provider authProvider) {
        this.userSeq = userSeq;
        this.id = id;
        this.password = password;
        this.birthDate = birthDate;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.authProvider = authProvider;
    }
}
