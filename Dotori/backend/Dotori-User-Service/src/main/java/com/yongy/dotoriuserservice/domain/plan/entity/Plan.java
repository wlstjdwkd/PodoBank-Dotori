package com.yongy.dotoriuserservice.domain.plan.entity;

import com.yongy.dotoriuserservice.domain.account.entity.Account;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity(name="plans")
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="plan_seq")
    private Long planSeq;

    private Long userSeq;

    @OneToOne
    @JoinColumn(name = "accountSeq")
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(name="plan_state", nullable = false)
    private State planState;

    @Column(name="total_savings", nullable = false)
    private BigDecimal totalSavings;

    @Column(name="additional_savings", nullable = false)
    private BigDecimal additionalSavings;

    @Column(name="save_at")
    private LocalDateTime saveAt;

    @Column(name="start_at")
    private LocalDateTime startAt;

    @Column(name="end_at")
    private LocalDateTime endAt;

    @Column(name="terminate_at")
    private LocalDateTime terminatedAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;


    @Builder
    public Plan(Long planSeq, Long userSeq, Account account, State planState,
                BigDecimal totalSavings, BigDecimal additionalSavings, LocalDateTime saveAt,
                LocalDateTime startAt, LocalDateTime endAt, LocalDateTime terminatedAt, LocalDateTime updatedAt) {
        this.planSeq = planSeq;
        this.userSeq = userSeq;
        this.account = account;
        this.planState = planState;
        this.totalSavings = totalSavings;
        this.additionalSavings = additionalSavings;
        this.saveAt = saveAt;
        this.startAt = startAt;
        this.endAt = endAt;
        this.terminatedAt = terminatedAt;
        this.updatedAt = updatedAt;
    }

    public void update(Plan plan){
        this.terminatedAt = plan.terminatedAt;
        this.planState = plan.planState;
    }

    public Plan terminate(LocalDateTime localDateTime){
        this.terminatedAt = localDateTime;
        return this;
    }

    public Plan updateState(State state){
        this.planState = state;
        return this;
    }
}