package com.yongy.dotori.domain.reward.entity;

import com.yongy.dotori.domain.user.entity.User;
// import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
