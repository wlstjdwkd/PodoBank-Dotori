package com.yongy.dotori.domain.bank.entity;

import com.yongy.dotori.domain.account.entity.Account;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Entity(name="banks")
public class Bank {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="bank_seq")
    private Long bankSeq;

    @Column(name="bank_name", nullable = false)
    private String bankName;

    @OneToMany(mappedBy = "bank")
    private List<Account> accountList;

    @Builder
    public Bank(Long bankSeq, String bankName) {
        this.bankSeq = bankSeq;
        this.bankName = bankName;
    }
}
