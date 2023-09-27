package com.yongy.dotoripurposeservice.domain.purposeData.entity;

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

    private Long purposeSeq;

    private Long accountSeq;

    @Column(name="data_amount", nullable = false)
    private BigDecimal dataAmount;

    @Column(name="data_current_balance", nullable = false)
    private BigDecimal dataCurrentBalance;

    @Column(name="data_created_at", nullable = false)
    private LocalDateTime dataCreatedAt;

}
