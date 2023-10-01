package com.yongy.dotorimainservice.domain.categoryGroup.entity;


import com.yongy.dotorimainservice.domain.planDetail.entity.PlanDetail;
import com.yongy.dotorimainservice.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity(name = "category_groups")
@NoArgsConstructor
public class CategoryGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_group_seq")
    private Long categoryGroupSeq;

    @ManyToOne
    @JoinColumn(name = "userSeq")
    private User user;

    @Column(name = "group_title", nullable = false)
    private String groupTitle;

    @OneToMany(mappedBy = "categoryGroup")
    private List<PlanDetail> planDetailList;


    @Builder
    public CategoryGroup(Long categoryGroupSeq, User user, String groupTitle) {
        this.categoryGroupSeq = categoryGroupSeq;
        this.user = user;
        this.groupTitle = groupTitle;
    }
}
