package com.yongy.dotori.domain.purposeData.entity;

import com.yongy.dotori.domain.account.entity.Account;
import com.yongy.dotori.domain.purpose.entity.Purpose;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity(name="purpose_data")
public class PurposeData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="purpose_data_seq")
    private Long purposeDataSeq;

    @ManyToOne
    @JoinColumn(name="purposeSeq")
    private Purpose purpose;

    @ManyToOne
    @JoinColumn(name="accountSeq")
    private Account account;

    @Column(name="data_amount", nullable = false)
    private BigDecimal dataAmount;

    @Column(name="data_current_balance", nullable = false)
    private BigDecimal dataCurrentBalance;

    @Column(name="data_created_at", nullable = false)
    private LocalDateTime dataCreatedAt;

}
