package com.yongy.dotori.domain.purposeData.entity;

import com.yongy.dotori.domain.account.entity.Account;
import com.yongy.dotori.domain.purpose.entity.Purpose;
// import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
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

    @Column(name="data_name", nullable = false)
    private String dataName;

    @Column(name="data_amount", nullable = false)
    private BigDecimal dataAmount;

    @Column(name="data_current_balance", nullable = false)
    private BigDecimal dataCurrentBalance;

    @Column(name="data_created_at", nullable = false)
    private LocalDateTime dataCreatedAt;

    @Builder
    public PurposeData(Long purposeDataSeq, Purpose purpose, Account account, String dataName,
                       BigDecimal dataAmount, BigDecimal dataCurrentBalance, LocalDateTime dataCreatedAt) {
        this.purposeDataSeq = purposeDataSeq;
        this.purpose = purpose;
        this.account = account;
        this.dataName = dataName;
        this.dataAmount = dataAmount;
        this.dataCurrentBalance = dataCurrentBalance;
        this.dataCreatedAt = dataCreatedAt;
    }
}
