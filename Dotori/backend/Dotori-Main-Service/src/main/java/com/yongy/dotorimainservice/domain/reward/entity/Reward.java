package com.yongy.dotorimainservice.domain.reward.entity;

import com.yongy.dotori.domain.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity(name = "reward")
@NoArgsConstructor
public class Reward implements Serializable {

    @Id
    @OneToOne
    @JoinColumn(name = "userSeq")
    private User user;
    private Long dotori;
    private Long coin;
}
