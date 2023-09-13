package com.yongy.dotori.domain.purpose.entity;

import com.yongy.dotori.domain.purposeData.entity.PurposeData;
import com.yongy.dotori.domain.user.entity.User;
// import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Entity(name="purposes")
public class Purpose {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(name="start_at", nullable = false)
    private LocalDateTime startAt;

    @Column(name="end_at", nullable = false)
    private LocalDateTime endAt;

    @Column(name="is_terminated", nullable = false)
    private boolean isTerminated;

    @Column(name="termination_at", nullable = false)
    private LocalDateTime terminationAt;

    @OneToMany(mappedBy = "purpose")
    private List<PurposeData> purposeDataList;

    @Builder
    public Purpose(Long purposeSeq, User user, String purposeTitle, BigDecimal goalAmount,
                   BigDecimal currentBalance, LocalDateTime startAt, LocalDateTime endAt, boolean isTerminated, LocalDateTime terminationAt) {
        this.purposeSeq = purposeSeq;
        this.user = user;
        this.purposeTitle = purposeTitle;
        this.goalAmount = goalAmount;
        this.currentBalance = currentBalance;
        this.startAt = startAt;
        this.endAt = endAt;
        this.isTerminated = isTerminated;
        this.terminationAt = terminationAt;
    }
}
