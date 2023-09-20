package com.yongy.dotori.domain.plan.service;

import com.yongy.dotori.domain.account.repository.AccountRepository;
import com.yongy.dotori.domain.category.entity.Category;
import com.yongy.dotori.domain.category.repository.CategoryRepository;
import com.yongy.dotori.domain.categoryGroup.entity.CategoryGroup;
import com.yongy.dotori.domain.categoryGroup.repository.CategoryGroupRepository;
import com.yongy.dotori.domain.plan.dto.CategoryDTO;
import com.yongy.dotori.domain.plan.dto.CategoryGroupListDTO;
import com.yongy.dotori.domain.plan.dto.PlanDTO;
import com.yongy.dotori.domain.plan.entity.Plan;
import com.yongy.dotori.domain.plan.entity.State;
import com.yongy.dotori.domain.plan.repository.PlanRepository;
import com.yongy.dotori.domain.planDetail.entity.PlanDetail;
import com.yongy.dotori.domain.planDetail.repository.PlanDetailRepository;
import com.yongy.dotori.domain.user.entity.User;
import com.yongy.dotori.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService{

    private final PlanRepository planRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final CategoryGroupRepository categoryGroupRepository;
    private final CategoryRepository categoryRepository;
    private final PlanDetailRepository planDetailRepository;

    @Override
    public void createPlan(PlanDTO planDTO) {
        //User user = User.getLoginUser();
        User loginUser = userRepository.findById("1");

        Plan plan = planRepository.save(Plan.builder()
                        .user(loginUser)
                        .account(accountRepository.findByAccountSeq(planDTO.getAccountSeq()))
                        .startAt(planDTO.getStartedAt())
                        .endAt(planDTO.getEndAt())
                        .planState(State.ACTIVE)
                        .saveAt(planDTO.getEndAt())
                        .additionalSavings(BigDecimal.ZERO)
                        .totalSavings(BigDecimal.ZERO)
                .build());

        List<CategoryGroupListDTO> groupList = planDTO.getCategoryGroupList();
        for(CategoryGroupListDTO group : groupList){
            // 카테고리 그룹 만들기
            CategoryGroup categoryGroup= categoryGroupRepository.save(CategoryGroup.builder()
                            .user(loginUser)
                    .groupTitle(group.getGroupTitle()).build());

            // 카테고리 만들기 +  Plan에 딸린 실행중인 카테고리인 PlanDetail 생성
            List<CategoryDTO> categorise = group.getCategoryDTOList();
            for(CategoryDTO data : categorise){
                Category category = categoryRepository.save(Category.builder()
                                .user(loginUser)
                                .categoryTitle(data.getCategoryName())
                        .build());

                planDetailRepository.save(PlanDetail.builder()
                                .plan(plan)
                                .category(category)
                                .categoryGroup(categoryGroup)
                                .detailLimit(data.getTargetAmount())
                                .detailBalance(BigDecimal.ZERO)
                        .build());
            }
        }
    }

    @Transactional
    @Override
    public void terminatePlan(Long planSeq) {
        // 계획 중단시 plan_state를 INACTIVE로 변경
        Plan plan = planRepository.findByPlanSeq(planSeq);

        // TODO 진행중인 계획이 아니면 Exception 던지기
        if(plan.getPlanState() != State.ACTIVE){

        }

        plan.update(Plan.builder()
                .endAt(LocalDateTime.now())
                .planState(State.INACTIVE)
                .build());
    }

//        private User getLoginUser() {
//        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//    }
}
