package com.yongy.dotorimainservice.domain.purpose.entity;

import com.yongy.dotori.domain.purposeData.entity.PurposeData;
import com.yongy.dotori.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "userSeq")
    private User user;

    @Column(name="purpose_title", nullable = false)
    private String purposeTitle;

    @Column(name="goal_amount", nullable = false)
    private BigDecimal goalAmount;

    @Column(name="current_balance", nullable = false)
    private BigDecimal currentBalance;

    @Column(name="started_at", nullable = false)
    private LocalDate startedAt;

    @Column(name="end_at", nullable = false)
    private LocalDate endAt;

    @Column(name="terminate_at")
    private LocalDateTime terminatedAt;

    @OneToMany(mappedBy = "purpose")
    private List<PurposeData> purposeDataList;

    public void update(Purpose purpose){
        if(purpose.endAt != null){
            this.endAt = purpose.endAt;
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
