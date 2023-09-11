package com.yongy.dotori.domain.purpose.entity;

import com.yongy.dotori.domain.purposeData.entity.PurposeData;
import com.yongy.dotori.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity(name="purposes")
public class Purpose {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="purpose_seq")
    private Long purposeSeq;

    @ManyToOne
    @JoinColumn(name = "id")
    private User user;

    @Column(name="purpose_title", nullable = false)
    private String purposeTitle;

    @Column(name="goal_amount", nullable = false)
    private BigDecimal goalAmount;

    @Column(name="current_balance", nullable = false)
    private BigDecimal currentBalance;

    @Column(name="started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name="end_at", nullable = false)
    private LocalDateTime endAt;

    @Column(name="is_terminated", nullable = false)
    private boolean isTerminated;

    @Column(name="terminated_at")
    private LocalDateTime terminatedAt;

    @OneToMany(mappedBy = "purpose")
    private List<PurposeData> purposeDataList;

    @Builder
    public Purpose(Long purposeSeq, User user, String purposeTitle, BigDecimal goalAmount,
                   BigDecimal currentBalance, LocalDateTime startedAt, LocalDateTime endAt, boolean isTerminated, LocalDateTime terminatedAt) {
        this.purposeSeq = purposeSeq;
        this.user = user;
        this.purposeTitle = purposeTitle;
        this.goalAmount = goalAmount;
        this.currentBalance = currentBalance;
        this.startedAt = startedAt;
        this.endAt = endAt;
        this.isTerminated = isTerminated;
        this.terminatedAt = terminatedAt;
    }
}
