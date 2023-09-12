package com.yongy.dotori.domain.account.entity;

import com.yongy.dotori.domain.bank.entity.Bank;
import com.yongy.dotori.domain.plan.entity.Plan;
import com.yongy.dotori.domain.purposeData.entity.PurposeData;
import com.yongy.dotori.domain.user.entity.User;
// import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Entity(name="accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="account_seq")
    private Long accountSeq;

    @ManyToOne
    @JoinColumn(name = "userSeq")
    private User user;

    @ManyToOne
    @JoinColumn(name = "bankSeq")
    private Bank bank;

    private Long accountNumber;

    private String accessToken;

    private String accountTitle;

    private LocalDateTime deleteAt;

    @OneToMany(mappedBy = "account")
    private List<PurposeData> purposeDataList;

    @OneToOne(mappedBy = "account")
    private Plan plan;

    @Builder
    public Account(Long accountSeq, User user, Bank bank, Long accountNumber,
                   String accessToken, String accountTitle, LocalDateTime deleteAt) {
        this.accountSeq = accountSeq;
        this.user = user;
        this.bank = bank;
        this.accountNumber = accountNumber;
        this.accessToken = accessToken;
        this.accountTitle = accountTitle;
        this.deleteAt = deleteAt;
    }
}
