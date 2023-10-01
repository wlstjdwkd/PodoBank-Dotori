package com.yongy.dotorimainservice.domain.plan.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yongy.dotorimainservice.domain.account.entity.Account;
import com.yongy.dotorimainservice.domain.account.repository.AccountRepository;
import com.yongy.dotorimainservice.domain.account.service.AccountService;
import com.yongy.dotorimainservice.domain.bank.entity.Bank;
import com.yongy.dotorimainservice.domain.bank.repository.BankRepository;
import com.yongy.dotorimainservice.domain.category.entity.Category;
import com.yongy.dotorimainservice.domain.category.repository.CategoryRepository;
import com.yongy.dotorimainservice.domain.categoryGroup.entity.CategoryGroup;
import com.yongy.dotorimainservice.domain.categoryGroup.repository.CategoryGroupRepository;
import com.yongy.dotorimainservice.domain.plan.dto.*;
import com.yongy.dotorimainservice.domain.plan.dto.response.PlanListDto;
import com.yongy.dotorimainservice.domain.plan.entity.Plan;
import com.yongy.dotorimainservice.domain.plan.entity.State;
import com.yongy.dotorimainservice.domain.plan.exception.NotActivePlanException;
import com.yongy.dotorimainservice.domain.plan.exception.PurposeServiceFailedException;
import com.yongy.dotorimainservice.domain.plan.repository.PlanRepository;
import com.yongy.dotorimainservice.domain.planDetail.entity.PlanDetail;
import com.yongy.dotorimainservice.domain.planDetail.repository.PlanDetailRepository;
import com.yongy.dotorimainservice.domain.user.entity.User;
import com.yongy.dotorimainservice.global.common.CallServer;
import com.yongy.dotorimainservice.global.common.PodoBankInfo;
import com.yongy.dotorimainservice.global.redis.entity.BankAccessToken;
import com.yongy.dotorimainservice.global.redis.repository.BankAccessTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;
    private final PlanDetailRepository planDetailRepository;
    private final CategoryGroupRepository categoryGroupRepository;
    private final CategoryRepository categoryRepository;
    private final AccountRepository accountRepository;
    private final BankRepository bankRepository;
    private final BankAccessTokenRepository bankAccessTokenRepository;
    private final AccountService accountService;
    private final CallServer callServer;
    private final PodoBankInfo podoBankInfo;
    @Value("${dotori.purpose.url}")
    private String PURPOSE_SERVICE_URL;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void createPlan(PlanDTO planDTO) {
        User loginUser = this.getLoginUser();
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime = LocalDateTime.parse(planDTO.getStartedAt(), formatter);

        State state = State.READY;
        if(startTime.toLocalDate().equals(LocalDate.now())){
            state = State.ACTIVE;
        }

        Plan plan = planRepository.save(Plan.builder()
                .userSeq(loginUser.getUserSeq())
                .account(accountRepository.findByAccountSeqAndDeleteAtIsNull(planDTO.getAccountSeq()))
                .startAt(LocalDateTime.parse(planDTO.getStartedAt(), formatter))
                .endAt(LocalDateTime.parse(planDTO.getEndAt(), formatter))
                .planState(state)
                .updatedAt(LocalDateTime.parse(planDTO.getStartedAt(), formatter)) // 마지막 업데이트 날짜
                .totalSavings(BigDecimal.ZERO)
                .count(0L)
                .build());

        List<CategoryGroupListDTO> groupList = planDTO.getCategoryGroupList();
        for (CategoryGroupListDTO group : groupList) {
            // 카테고리 그룹 만들기
            CategoryGroup categoryGroup = categoryGroupRepository.save(CategoryGroup.builder()
                    .user(loginUser)
                    .groupTitle(group.getCategoryGroupName()).build());

            // 카테고리 만들기 +  Plan에 딸린 실행중인 카테고리인 PlanDetail 생성
            List<ActiveCategoryDTO> categorise = group.getCategories();
            log.info(categorise.isEmpty()+"");
            List<PlanDetail> planDetailList = new ArrayList<>();
            for (ActiveCategoryDTO data : categorise) {
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
            planDetailRepository.saveAll(planDetailList);
        }
    }

    @Override
    @Transactional
    public void terminatePlan(Long planSeq) {
        // 계획 중단시 plan_state를 INACTIVE로 변경
        Plan plan = planRepository.findByPlanSeq(planSeq);

        if (plan.getPlanState() != State.ACTIVE) {
            throw new NotActivePlanException("진행 중인 계획이 아닙니다.");
        }

        plan.update(Plan.builder()
                .terminatedAt(LocalDateTime.now())
                .planState(State.INACTIVE)
                .build());
    }

    @Override
    public void updateState(State state, PlanStateDTO planStateDTO) {
        Plan plan = planRepository.findByPlanSeq(planStateDTO.getPlanSeq());

        if(!plan.getPlanState().equals(State.ACTIVE)){
            throw new NotActivePlanException("진행 중인 계획이 아닙니다.");
        }

        plan.updateState(state);
    }


    @Override
    public void saving(SavingDTO savingDTO) {
        log.info(savingDTO.getPlanSeq()+"");

        Plan plan = planRepository.findByPlanSeq(savingDTO.getPlanSeq());
        Account account = plan.getAccount();

        if(!plan.getPlanState().equals(State.ACTIVE)){
            throw new IllegalArgumentException("실행 중인 계획이 아닙니다.");
        }

        HashMap<String, Object> data = new HashMap<>();
        data.put("accountSeq",plan.getAccount().getAccountSeq());
        data.put("savingDTO",(savingDTO));

        ResponseEntity<String> response = callServer.postHttpBodyAndSend(PURPOSE_SERVICE_URL+"/purpose/saving", data);
        if(!response.getStatusCode().equals(HttpStatusCode.valueOf(200))){
            throw new PurposeServiceFailedException("Purpose Service 호출 에러");
        }

        // 2. 계획 종료 표시하기
        planRepository.save(plan.updateState(State.SAVED));

        // NOTE : 모든 확인이 되면 은행에 출금 요청 보내기
        callBankAPI(account, savingDTO);
    }

    public void callBankAPI(Account account, SavingDTO savingDTO){
        Bank bankInfo = bankRepository.findByBankSeq(account.getBank().getBankSeq());
        log.info(bankInfo.getBankName());
        BankAccessToken bankAccessToken = bankAccessTokenRepository.findByBankName(bankInfo.getBankName()); // 은행 accessToken 가져오기
        String accessToken = null;

        if(bankAccessToken != null){ // accessToken이 없으면
            accessToken = bankAccessToken.getToken();
        }

        if(bankAccessToken == null){
            accessToken = podoBankInfo.getConnectionToken(bankInfo.getBankSeq());
        }
        // NOTE : plan에 연결된 계좌에서 총 금액을 도토리 계좌로 입금 요청하기
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=utf-8");
        httpHeaders.add("Authorization","Bearer " + accessToken);

        Map<String, String> bodyData = new HashMap<>();
        bodyData.put("serviceCode", bankInfo.getServiceCode());
        bodyData.put("fintechCode", account.getFintechCode());
        bodyData.put("amount", savingDTO.getTotalSaving().toString());
        bodyData.put("content", "도토리 저축");

        HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(bodyData, httpHeaders);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(
                bankInfo.getBankUrl()+"/api/v1/fintech/withdraw",
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        String responseCode = response.getStatusCode().toString().split(" ")[0];
        if(!responseCode.equals("200")){
            throw new IllegalArgumentException("출금에 실패했습니다.");
        }
    }

    @Override
    public List<PlanListDto> getPlanList(Long userSeq) {
        List<Plan> planList = planRepository.findAllByUserSeqAndTerminatedAtIsNull(userSeq);
        List<PlanListDto> planListDtoList = new ArrayList<>();
        for(Plan plan : planList){
            planListDtoList.add(PlanListDto.builder().planSeq(plan.getPlanSeq())
                    .startAt(plan.getStartAt().format(formatter))
                    .endAt(plan.getEndAt().format(formatter)).build());
        }
        return planListDtoList;
    }

    @Override
    public ActivePlanDTO findAllPlan(Long accountSeq) throws JsonProcessingException {
//         실행중인 계획 리스트 조회
        Account account = accountRepository.findByAccountSeqAndDeleteAtIsNull(accountSeq);
        Plan plan = planRepository.findByAccountAccountSeq(accountSeq);

        // Plan이 있는 지 확인, 실행중인 Plan인지 확인
        // 플랜이 있고, 실행중이면 : 로직 처리
        // 플랜이 있고, 시작 전인 플랜이 있으면
        // 플랜이 없으면 : 플랜 만들기 페이지

        if((plan != null && plan.getPlanState().equals(State.ACTIVE)) || (plan != null && plan.getPlanState().equals(State.READY))){
            // 실행 중인 카테고리 가져오기
            List<PlanDetail> planDetailList = plan.getPlanDetailList();
            List<ActivePlanDetailDTO> activePlanList = new ArrayList<>();

            for(int i = 0; i < planDetailList.size(); i++){
                PlanDetail planDetail = planDetailList.get(i);
                activePlanList.add(ActivePlanDetailDTO.builder()
                                .title(planDetail.getCategory().getCategoryTitle())
                                .groupTitle(planDetail.getCategoryGroup().getGroupTitle())
                                .goalAmount(planDetail.getDetailLimit())
                                .currentBalance(planDetail.getDetailBalance())
                        .build());
            }

            ActivePlanDTO result = ActivePlanDTO.builder()
                    .accountBalance(accountService.getBalance(accountSeq))
                    .startedAt(plan.getStartAt())
                    .endAt(plan.getEndAt())
                    .state(plan.getPlanState())
                    .planSeq(plan.getPlanSeq())
                    .terminatedAt(plan.getTerminatedAt())
                    .unclassified(plan.getCount())
                    .activePlanList(activePlanList)
                .build();

            return result;
        }
        return ActivePlanDTO.builder().accountBalance(accountService.getBalance(accountSeq)).build();
    }

    @Override
    public void removeUserPlans(Long userSeq){
        List<Plan> planList = planRepository.findAllByUserSeqAndTerminatedAtIsNull(userSeq);
        for(Plan plan : planList){
            plan.setPlanState(State.INACTIVE);
            plan.setEndAt(LocalDateTime.now());
        }
        planRepository.saveAll(planList);
    }

    public User getLoginUser(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}