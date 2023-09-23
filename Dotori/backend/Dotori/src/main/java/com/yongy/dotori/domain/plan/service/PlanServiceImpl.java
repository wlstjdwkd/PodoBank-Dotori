package com.yongy.dotori.domain.plan.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yongy.dotori.domain.account.dto.BodyDataDTO;
import com.yongy.dotori.domain.account.entity.Account;
import com.yongy.dotori.domain.account.repository.AccountRepository;
import com.yongy.dotori.domain.bank.entity.Bank;
import com.yongy.dotori.domain.bank.repository.BankRepository;
import com.yongy.dotori.domain.category.entity.Category;
import com.yongy.dotori.domain.category.repository.CategoryRepository;
import com.yongy.dotori.domain.categoryGroup.entity.CategoryGroup;
import com.yongy.dotori.domain.categoryGroup.repository.CategoryGroupRepository;
import com.yongy.dotori.domain.plan.dto.*;
import com.yongy.dotori.domain.plan.entity.Plan;
import com.yongy.dotori.domain.plan.entity.State;
import com.yongy.dotori.domain.plan.repository.PlanRepository;
import com.yongy.dotori.domain.planDetail.entity.PlanDetail;
import com.yongy.dotori.domain.planDetail.repository.PlanDetailRepository;
import com.yongy.dotori.domain.purpose.entity.Purpose;
import com.yongy.dotori.domain.purpose.repository.PurposeRepository;
import com.yongy.dotori.domain.purposeData.entity.PurposeData;
import com.yongy.dotori.domain.purposeData.repository.PurposeDataRepository;
import com.yongy.dotori.domain.user.entity.User;
import com.yongy.dotori.domain.userAuth.service.UserAuthService;
//import com.yongy.dotori.global.redis.repository.FintechTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpHead;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.BigInteger;
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
    private final AccountRepository accountRepository;
    private final CategoryGroupRepository categoryGroupRepository;
    private final CategoryRepository categoryRepository;
    private final PlanDetailRepository planDetailRepository;
    private final PurposeRepository purposeRepository;
    private final BankRepository bankRepository;
    private final UserAuthService userAuthService;
   // private final FintechTokenRepository fintechTokenRepository;
    private final PurposeDataRepository purposeDataRepository;

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @Override
    public void createPlan(PlanDTO planDTO) {
        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        log.info(accountRepository.findByAccountSeq(planDTO.getAccountSeq())+"");
        Plan plan = planRepository.save(Plan.builder()
                .user(loginUser)
                .account(accountRepository.findByAccountSeq(planDTO.getAccountSeq()))
                .startAt(LocalDateTime.parse(planDTO.getStartedAt(), formatter))
                .endAt(LocalDateTime.parse(planDTO.getEndAt(), formatter))
                .planState(State.ACTIVE)
                .additionalSavings(BigDecimal.ZERO)
                .totalSavings(BigDecimal.ZERO)
                .build());

        List<CategoryGroupListDTO> groupList = planDTO.getCategoryGroupList();
        for (CategoryGroupListDTO group : groupList) {
            // 카테고리 그룹 만들기
            CategoryGroup categoryGroup = categoryGroupRepository.save(CategoryGroup.builder()
                    .user(loginUser)
                    .groupTitle(group.getGroupTitle()).build());

            // 카테고리 만들기 +  Plan에 딸린 실행중인 카테고리인 PlanDetail 생성
            List<CategoryDTO> categorise = group.getCategoryDTOList();
            for (CategoryDTO data : categorise) {
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
        if (plan.getPlanState() != State.ACTIVE) {

        }

        plan.update(Plan.builder()
                .endAt(LocalDateTime.now())
                .planState(State.INACTIVE)
                .build());
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
}