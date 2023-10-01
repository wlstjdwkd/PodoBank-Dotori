package com.yongy.dotorimainservice.domain.plan.entity;


import com.yongy.dotorimainservice.domain.account.entity.Account;
import com.yongy.dotorimainservice.domain.planDetail.entity.PlanDetail;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
    @Column(name="plan_state")
    private State planState;

    @Column(name="total_savings", nullable = false)
    private BigDecimal totalSavings;

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

    @Column(name = "unclassified_count", nullable = false)
    private Long count;

    @OneToMany(mappedBy = "plan")
    private List<PlanDetail> planDetailList;

    @Builder
    public Plan(Long planSeq, Long userSeq, Account account, State planState,
                BigDecimal totalSavings, LocalDateTime saveAt,
                LocalDateTime startAt, LocalDateTime endAt, LocalDateTime terminatedAt, LocalDateTime updatedAt, Long count) {
        this.planSeq = planSeq;
        this.userSeq = userSeq;
        this.account = account;
        this.planState = planState;
        this.totalSavings = totalSavings;
        this.saveAt = saveAt;
        this.startAt = startAt;
        this.endAt = endAt;
        this.terminatedAt = terminatedAt;
        this.updatedAt = updatedAt;
        this.count = count;
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

    public Plan updateCount(Long count){
        this.count += count;
        return this;
    }
}