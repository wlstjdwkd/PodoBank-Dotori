package com.bank.podo.domain.account.entity;

import com.bank.podo.global.others.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AccountCategory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountCategoryId;

    @Column
    private String accountType;

    @Column
    private String accountName;

    @Column
    private String accountDescription;

    @Column
    private BigDecimal interestRate;
}
