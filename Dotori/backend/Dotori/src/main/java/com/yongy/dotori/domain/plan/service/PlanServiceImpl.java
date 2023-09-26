package com.yongy.dotori.domain.plan.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yongy.dotori.domain.account.entity.Account;
import com.yongy.dotori.domain.account.repository.AccountRepository;
import com.yongy.dotori.domain.account.service.AccountServiceImpl;
import com.yongy.dotori.domain.bank.entity.Bank;
import com.yongy.dotori.domain.bank.repository.BankRepository;
import com.yongy.dotori.domain.category.entity.Category;
import com.yongy.dotori.domain.category.repository.CategoryRepository;
import com.yongy.dotori.domain.categoryGroup.entity.CategoryGroup;
import com.yongy.dotori.domain.categoryGroup.repository.CategoryGroupRepository;
import com.yongy.dotori.domain.plan.dto.*;
import com.yongy.dotori.domain.payment.repository.PaymentRepository;
import com.yongy.dotori.domain.plan.dto.*;
import com.yongy.dotori.domain.plan.dto.response.PlanListDto;
import com.yongy.dotori.domain.plan.entity.Plan;
import com.yongy.dotori.domain.plan.entity.State;
import com.yongy.dotori.domain.plan.exception.NotActivePlanException;
import com.yongy.dotori.domain.plan.exception.NotExistPlanException;
import com.yongy.dotori.domain.plan.exception.NotStartedPlanException;
import com.yongy.dotori.domain.plan.repository.PlanRepository;
import com.yongy.dotori.domain.planDetail.entity.PlanDetail;
import com.yongy.dotori.domain.planDetail.repository.PlanDetailRepository;
import com.yongy.dotori.domain.purpose.entity.Purpose;
import com.yongy.dotori.domain.purpose.repository.PurposeRepository;
import com.yongy.dotori.domain.purposeData.entity.PurposeData;
import com.yongy.dotori.domain.purposeData.repository.PurposeDataRepository;
import com.yongy.dotori.domain.user.entity.User;
import com.yongy.dotori.domain.user.repository.UserRepository;
import com.yongy.dotori.domain.userAuth.service.UserAuthService;
//import com.yongy.dotori.global.redis.repository.FintechTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;
    private final AccountRepository accountRepository;
    private final CategoryGroupRepository categoryGroupRepository;
    private final CategoryRepository categoryRepository;
    private final PlanDetailRepository planDetailRepository;
    private final PurposeRepository purposeRepository;
    private final BankRepository bankRepository;
    private final UserAuthService userAuthService;
    private final PurposeDataRepository purposeDataRepository;
    private final AccountServiceImpl accountService;
    private final UserRepository userRepository;
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
                .user(loginUser)
                .account(accountRepository.findByAccountSeqAndDeleteAtIsNull(planDTO.getAccountSeq()))
                .startAt(LocalDateTime.parse(planDTO.getStartedAt(), formatter))
                .endAt(LocalDateTime.parse(planDTO.getEndAt(), formatter))
                .planState(state)
                .updatedAt(LocalDateTime.parse(planDTO.getStartedAt(), formatter)) // 마지막 업데이트 날짜
                .additionalSavings(BigDecimal.ZERO)
                .totalSavings(BigDecimal.ZERO)
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

    @Transactional
    @Override
    public void terminatePlan(Long planSeq) {
        // 계획 중단시 plan_state를 INACTIVE로 변경
        Plan plan = planRepository.findByPlanSeq(planSeq);

        // TODO 진행중인 계획이 아니면 Exception 던지기
        if (plan.getPlanState() != State.ACTIVE) {
            throw new NotActivePlanException("진행 중인 계획이 아닙니다.");
        }

        plan.update(Plan.builder()
                .terminatedAt(LocalDateTime.now())
                .planState(State.INACTIVE)
                .build());
    }

    @Override
    public ActivePlanDTO findAllPlan(Long accountSeq) throws JsonProcessingException {
        // 실행중인 계획 리스트 조회
        Account account = accountRepository.findByAccountSeqAndDeleteAtIsNull(accountSeq);
        Plan plan = planRepository.findByAccountAccountSeq(accountSeq);

        // Plan이 있는 지 확인, 실행중인 Plan인지 확인
        // 플랜이 있고, 실행중이면 : 로직 처리
        // 플랜이 있고, 시작 전인 플랜이 있으면
        // 플랜이 없으면 : 플랜 만들기 페이지

        if(plan != null && plan.getPlanState().equals(State.ACTIVE)){
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
                    .endAt(plan.getEndAt())
                    //.unclassified() // 미분류 어떻게 할 건지 정해야 됨!
                    .activePlanList(activePlanList)
                .build();

            return result;
        }

        if(plan != null && plan.getPlanState().equals(State.READY)){
            throw new NotStartedPlanException("아직 예약된 계획이 시작되지 않았습니다.");
        }

        return ActivePlanDTO.builder().accountBalance(accountService.getBalance(accountSeq)).build();
    }

    @Override
    public void updateState(PlanStateDTO planStateDTO) {
        Plan plan = planRepository.findByPlanSeq(planStateDTO.getPlanSeq());

        if(!plan.getPlanState().equals(State.ACTIVE)){
            throw new NotActivePlanException("실행 중인 계획이 아닙니다.");
        }

        plan.updateState(State.COMPLETED);
    }

    @Override
    public void saving(SavingDTO savingDTO) {
        Plan plan = planRepository.findByPlanSeq(savingDTO.getPlanSeq());
        Account account = plan.getAccount();

        List<PurposeData> purposeDataList = new ArrayList<>();
        BigDecimal totalSaving = new BigDecimal(BigInteger.ZERO);

        if(!plan.getPlanState().equals(State.ACTIVE)){
            throw new IllegalArgumentException("실행 중인 계획이 아닙니다.");
        }

        // 1. 각 목표에 따라 저축 금액 update 하기
        for (PurposeSavingDTO data : savingDTO.getPurposeSavingList()) {
            Purpose purpose = purposeRepository.findByPurposeSeq(data.getPurposeSeq());

            // 현재 금액에 저축 금액 더하기
            purpose.addCurrentBalance(data.getSavingAmount());
            purposeRepository.save(purpose);
            log.info(purpose.getCurrentBalance()+"");

            // 1-2. purpose_data 저장하기
            // 각 purpose에 연결된 purpose_data에 저장
            purposeDataList.add(PurposeData.builder()
                    .account(account)
                    .dataAmount(data.getSavingAmount())
                    .purpose(purpose)
                    .dataName(account.getAccountTitle())
                    .dataCurrentBalance(purpose.getCurrentBalance())
                    .dataCreatedAt(LocalDateTime.now())
                    .build());

            totalSaving = totalSaving.add(data.getSavingAmount());
        }

        if(!totalSaving.equals(savingDTO.getTotalSaving())){
            throw new IllegalArgumentException("총 저축 금액이 일치하지 않습니다.");
        }
        purposeDataRepository.saveAll(purposeDataList);

        // 2. 계획 종료 표시하기
        planRepository.save(plan.updateState(State.SAVED));

        // NOTE : 모든 확인이 되면 은행에 출금 요청 보내기
        callBankAPI(account, savingDTO);
    }

    public void callBankAPI(Account account, SavingDTO savingDTO){
        Bank bankInfo = bankRepository.findByBankSeq(account.getBank().getBankSeq());
        String accessToken = userAuthService.getConnectionToken(bankInfo.getBankSeq()); // 은행 accessToken 가져오기

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

    public List<PlanListDto> getPlanList(Long userSeq){
        List<Plan> planList = planRepository.findAllByUserUserSeqAndTerminatedAtIsNull(userSeq);
        List<PlanListDto> planListDtoList = null;
        for(Plan plan : planList){
            planListDtoList.add(PlanListDto.builder().planSeq(plan.getPlanSeq())
                    .startAt(plan.getStartAt().format(formatter))
                    .endAt(plan.getEndAt().format(formatter)).build());
        }
        return planListDtoList;
    }

    // NOTE : 12시 되면 날짜 확인하고 실행
    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    public void startEndPlan(){
        // 모든 사용자의 모든 READY인 Plan 가져와서 ACTIVE로 변경하기
        LocalDateTime today = LocalDateTime.now();
        List<User> users = userRepository.findAll(); // 모든 사용자 가져오기

        for(User user : users){
            // 시작 처리
            List<Plan> startplans = planRepository.findAllByUserUserSeqAndPlanState(user.getUserSeq(), State.READY);
            List<Plan> startResult = new ArrayList<>();

            for(Plan p : startplans){
                if(p.getStartAt().toLocalDate().equals(today)){
                    startResult.add(p.updateState(State.ACTIVE));
                }
            }
            planRepository.saveAll(startResult);

            // 종료 처리
            List<Plan> endPlans = planRepository.findAllByEndAt(today);
            List<Plan> endResult = new ArrayList<>();

            for(Plan p : endPlans){
                if(p.getEndAt().toLocalDate().equals(today)){
                    endResult.add(p.terminate(today));
                }
            }
            planRepository.saveAll(endResult);
        }
    }

    public User getLoginUser(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}