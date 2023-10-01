package com.yongy.dotorimainservice.domain.reward.entity;

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
    private Long userSeq;
    private Long dotori;
    private Long coin;
}
