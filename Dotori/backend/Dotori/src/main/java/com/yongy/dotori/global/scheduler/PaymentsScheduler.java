package com.yongy.dotori.global.scheduler;


import com.yongy.dotori.domain.account.entity.Account;
import com.yongy.dotori.domain.account.repository.AccountRepository;
import com.yongy.dotori.domain.category.entity.Category;
import com.yongy.dotori.domain.category.repository.CategoryRepository;
import com.yongy.dotori.domain.categoryData.entity.CategoryData;
import com.yongy.dotori.domain.categoryData.repository.CategoryDataRepository;
import com.yongy.dotori.domain.chatGPT.dto.UnclassifiedDataDTO;
import com.yongy.dotori.domain.chatGPT.dto.UnclassifiedResponseDTO;
import com.yongy.dotori.domain.chatGPT.service.ChatGPTService;
import com.yongy.dotori.domain.payment.dto.response.PaymentPodoResDto;
import com.yongy.dotori.domain.payment.entity.Payment;
import com.yongy.dotori.domain.payment.repository.PaymentRepository;
import com.yongy.dotori.domain.payment.service.PaymentService;
import com.yongy.dotori.domain.plan.entity.Plan;
import com.yongy.dotori.domain.plan.entity.State;
import com.yongy.dotori.domain.plan.exception.PaymentUpdateBeforeException;
import com.yongy.dotori.domain.plan.repository.PlanRepository;
import com.yongy.dotori.domain.plan.service.PlanService;
import com.yongy.dotori.domain.planDetail.entity.PlanDetail;
import com.yongy.dotori.domain.planDetail.repository.PlanDetailRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class PaymentsScheduler {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private ChatGPTService chatGPTService;
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private CategoryDataRepository categoryDataRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PlanDetailRepository planDetailRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    //@Scheduled(fixedRate = 30 * 60 * 1000) // 30분(밀리초 단위)
    public void getPayments() throws ParseException, IOException {

        List<Plan> activePlanList = planRepository.findAllByPlanStateAndTerminatedAtIsNull(State.ACTIVE);

        LocalDateTime currentTime = LocalDateTime.now(); // 현재시간
        log.info("현재 시간: {}", currentTime);

        // 해당 플랜에 연결된 결제 내역
        for(Plan plan : activePlanList){
            // 현재 찾으려는 시간이 업데이트한 시간보다 이전이다.
            if(currentTime.isBefore(plan.getUpdatedAt())){
                continue;
            }

            List<PaymentPodoResDto> paymentResDto = paymentService.getPayments(plan.getUpdatedAt(),plan.getAccount().getAccountSeq());
            log.info(paymentResDto.toString());

            log.info(paymentResDto.size()+"");
            List<Payment> chatGPT = new ArrayList<>();
            List<Payment> existPayment = new ArrayList<>();

            for(PaymentPodoResDto payment : paymentResDto){
                // 카테고리 데이터에 정보가 없으면 최초로 들어온 정보이므로 GPT 분류
                CategoryData categoryData = categoryDataRepository.findByDataCode(payment.getCode());

                if(categoryData == null){
                    log.info(payment.getContent());
                    chatGPT.add(Payment.builder()
                            .paymentName(payment.getContent())
                            .paymentPrice(payment.getAmount())
                            .user(plan.getUser())
                            .checked(false)
                            .paymentDate(payment.getTransactionAt())
                            .build());
                    continue;
                }


                // 카테고리 데이터가 이미 있어서 planDetail에 연결 돼있는지 확인 해야하면
                // 카테고리데이터에 연결된 카테고리로 planDetail 찾기
                Category category = categoryRepository.findByCategorySeq(categoryData.getCategory().getCategorySeq());
                PlanDetail planDetail = planDetailRepository.findByCategory(category);

                if(plan == planDetail.getPlan()){ // 결제 내역을 가져온 플랜과 카테고리데이터에 연결된 카테고리에 연결된 플랜이 같으면
                    // 해당 planDetail정보를 payment에 저장하고, checked는 false로 해서 payment 생성
                    existPayment.add(Payment.builder()
                                    .paymentName(payment.getContent())
                                    .paymentPrice(payment.getAmount())
                                    .user(plan.getUser())
                                    .planDetail(planDetail)
                                    .checked(false)
                            .build());
                    continue;
                }

                chatGPT.add(Payment.builder()
                        .paymentName(payment.getContent())
                        .paymentPrice(payment.getAmount())
                        .user(plan.getUser())
                        .checked(false)
                        .build());
            }

            paymentRepository.saveAll(chatGPT); // chatGPT로 분류할 거 저장
            paymentRepository.saveAll(existPayment); // 이미 등록된 사업장인 payment 한 번에 저장

            // NOTE : chatGPT로 분류
            List<PlanDetail> planDetails = planDetailRepository.findAllByPlanPlanSeq(plan.getPlanSeq());
            chatGPTService.getPaymentChatGPTResponse(UnclassifiedDataDTO.builder()
                    .planDetails(planDetails)
                    .payments(chatGPT)
                    .build());
        }
    }


}
