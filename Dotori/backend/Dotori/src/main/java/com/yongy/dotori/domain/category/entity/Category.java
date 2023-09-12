package com.yongy.dotori.domain.category.entity;

import com.yongy.dotori.domain.categoryData.entity.CategoryData;
import com.yongy.dotori.domain.planDetail.entity.PlanDetail;
import com.yongy.dotori.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity(name = "categories")
@NoArgsConstructor
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_seq")
    private Long categorySeq;

    @ManyToOne
    @JoinColumn(name = "userSeq")
    private User user;

    @Column(name = "category_title", nullable = false)
    private String categoryTitle;

    @OneToMany(mappedBy = "category")
    private List<PlanDetail> planDetailList;

    @OneToMany(mappedBy = "category")
    private List<CategoryData> categoryDataList;

    @Builder
    public Category(Long categorySeq, User user, String categoryTitle) {
        this.categorySeq = categorySeq;
        this.user = user;
        this.categoryTitle = categoryTitle;
    }
}
