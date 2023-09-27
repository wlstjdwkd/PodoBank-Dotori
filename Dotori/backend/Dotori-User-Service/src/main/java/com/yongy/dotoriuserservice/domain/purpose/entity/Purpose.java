package com.yongy.dotoriuserservice.domain.purpose.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="purposes")
public class Purpose {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="purpose_seq")
    private Long purposeSeq;

    private Long userId;

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

    @Column(name="terminate", nullable = false)
    private boolean terminated;

    @Column(name="terminate_at")
    private LocalDateTime terminatedAt;

    public void update(Purpose purpose){
        if(purpose.endAt != null){
            this.endAt = purpose.endAt;
        }
        if(!purpose.terminated){
            this.terminated = true;
        }
        if(purpose.terminatedAt != null){
            this.terminatedAt = purpose.terminatedAt;
        }
        if(purpose.currentBalance != null){
            this.currentBalance = purpose.currentBalance;
        }
    }

    public Purpose addCurrentBalance(BigDecimal amount){
        this.currentBalance = this.currentBalance.add(amount);
        return this;
    }
}
