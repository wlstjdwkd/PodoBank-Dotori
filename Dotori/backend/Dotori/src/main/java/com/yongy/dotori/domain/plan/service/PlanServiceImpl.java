package com.yongy.dotori.domain.plan.service;

import com.yongy.dotori.domain.account.entity.Account;
import com.yongy.dotori.domain.account.repository.AccountRepository;
import com.yongy.dotori.domain.category.entity.Category;
import com.yongy.dotori.domain.category.repository.CategoryRepository;
import com.yongy.dotori.domain.categoryGroup.entity.CategoryGroup;
import com.yongy.dotori.domain.categoryGroup.repository.CategoryGroupRepository;
import com.yongy.dotori.domain.payment.repository.PaymentRepository;
import com.yongy.dotori.domain.plan.dto.*;
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
import java.util.ArrayList;
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
    private final PaymentRepository paymentRepository;

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

        // 미분류 PlanDetail(카테고리) 만들기
        planDetailRepository.save(PlanDetail.builder()
                .plan(plan)
                .category(categoryRepository.save(Category.builder().user(loginUser).categoryTitle("미분류").build()))
                .categoryGroup(categoryGroupRepository.save(CategoryGroup.builder().user(loginUser).groupTitle("미분류").build()))
                .detailLimit(BigDecimal.ZERO)
                .detailBalance(BigDecimal.ZERO)
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
                                .categoryTitle(data.getCategoryTitle())
                        .build());

                planDetailRepository.save(PlanDetail.builder()
                                .plan(plan)
                                .category(category)
                                .categoryGroup(categoryGroup)
                                .detailLimit(data.getGoal_amount())
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

    @Override
    public ActivePlanDTO findAllPlan(Long accountSeq) {
        // 실행중인 계획 리스트 조회
        Account account = accountRepository.findByAccountSeq(accountSeq);
        Plan plan = planRepository.findByAccountAccountSeq(accountSeq);

        // Plan이 있는 지 확인, 실행중인 Plan인지 확인
        // 플랜이 있고, 실행중이면 : 로직 처리
        // 플랜이 있고, 실행중인 플랜이 없으면 : 플랜 만들기 페이지
        // 플랜이 없으면 : 플랜 만들기 페이지

        //
        //if(plan != null && plan.getPlanState() == State.ACTIVE){
            log.info("들어옴");
            // 실행 중인 카테고리 가져오기
            List<PlanDetail> planDetailList = plan.getPlanDetailList();
            List<ActivePlanDetailDTO> activePlanList = new ArrayList<>();

            for(int i = 1; i < planDetailList.size(); i++){
                PlanDetail planDetail = planDetailList.get(i);
                activePlanList.add(ActivePlanDetailDTO.builder()
                                .title(planDetail.getCategory().getCategoryTitle())
                                .groupTitle(planDetail.getCategoryGroup().getGroupTitle())
                                .goalAmount(planDetail.getDetailLimit())
                                .currentBalance(planDetail.getDetailBalance())
                        .build());
            }

            ActivePlanDTO result = ActivePlanDTO.builder()
                    // TODO 계좌 잔액 가져오기 : 포도은행 API 사용
                    .accountBalance(new BigDecimal("123456"))
                    .endAt(plan.getEndAt())
                    .unclassified(paymentRepository.countByPlanDetailPlanDetailSeq(planDetailList.get(0).getPlanDetailSeq()))
                    .activePlanList(activePlanList)
                .build();

            return result;
        //}

//        log.info("안들어옴");
//        return null;
    }

//        private User getLoginUser() {
//        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//    }
}
