package com.yongy.dotori.domain.user.entity;

import com.yongy.dotori.domain.account.entity.Account;
import com.yongy.dotori.domain.category.entity.Category;
import com.yongy.dotori.domain.categoryGroup.entity.CategoryGroup;
import com.yongy.dotori.domain.payment.entity.Payment;
import com.yongy.dotori.domain.plan.entity.Plan;
import com.yongy.dotori.domain.reward.entity.Reward;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_seq")
    private Long userSeq;

    @Enumerated(EnumType.STRING)
//    @ColumnDefault(value="ROLE_USER")
    private Role role;

    @Column(nullable = false)
    private String id;

    private String password;

    private LocalDate birthDate;

    @Column(name="user_name", nullable = false)
    private String userName;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name="auth_provider", nullable = false)
    private Provider authProvider;

    @Column(name="expired_at", nullable = true)
    private LocalDateTime expiredAt;

//    @OneToMany(mappedBy = "user")
//    private List<Purpose> purposeList;

    @OneToMany(mappedBy = "user")
    private List<Account> accountList;

    @OneToMany(mappedBy = "user")
    private List<Plan> planList;

    @OneToMany(mappedBy = "user")
    private List<Category> categoryList;

    @OneToMany(mappedBy = "user")
    private List<CategoryGroup> categoryGroupList;

    @OneToMany(mappedBy = "user")
    private List<Payment> paymentList;

    @OneToOne(mappedBy = "user")
    private Reward reward;


    @Builder
    public User(Long userSeq,Role role, String id, String password, LocalDate birthDate, String userName, String phoneNumber, Provider authProvider, LocalDateTime expiredAt) {
        this.userSeq = userSeq;
        this.role = role;
        this.id = id;
        this.password = password;
        this.birthDate = birthDate;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.authProvider = authProvider;
        this.expiredAt = expiredAt;
    }
}
