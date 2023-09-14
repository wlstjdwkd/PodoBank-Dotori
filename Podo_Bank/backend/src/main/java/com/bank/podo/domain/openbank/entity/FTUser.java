package com.bank.podo.domain.openbank.entity;

import com.bank.podo.domain.account.entity.Account;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FTUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fintechId;

    @Column(nullable = false)
    private String fintechCode;

    @Column
    private boolean locked;

    @Column
    private LocalDateTime createAt;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private FTService ftService;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Builder
    public FTUser(String fintechCode, boolean locked, FTService ftService, Account account) {
        this.fintechCode = fintechCode;
        this.locked = locked;
        this.createAt = LocalDateTime.now();
        this.ftService = ftService;
        this.account = account;
    }
}
