package com.yongy.dotori.domain.plan.entity;

import com.yongy.dotori.domain.account.entity.Account;
import com.yongy.dotori.domain.planDetail.entity.PlanDetail;
import com.yongy.dotori.domain.user.entity.User;
//import jakarta.persistence.*;
import lombok.*;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="plans")
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="plan_seq")
    private Long planSeq;

    @ManyToOne
    @JoinColumn(name = "userSeq")
    private User user;

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

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "plan")
    private List<PlanDetail> planDetailList;


    public void update(Plan plan){
        this.endAt = plan.endAt;
        this.planState = plan.planState;
    }

    public Plan updateState(State state){
        this.planState = state;
        return this;
    }
}
