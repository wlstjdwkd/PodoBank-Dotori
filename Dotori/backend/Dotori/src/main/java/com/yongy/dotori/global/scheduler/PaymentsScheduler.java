package com.yongy.dotori.global.scheduler;


import com.yongy.dotori.domain.account.entity.Account;
import com.yongy.dotori.domain.account.repository.AccountRepository;
import com.yongy.dotori.domain.payment.dto.response.PaymentPodoResDto;
import com.yongy.dotori.domain.payment.service.PaymentService;
import com.yongy.dotori.domain.plan.entity.Plan;
import com.yongy.dotori.domain.plan.entity.State;
import com.yongy.dotori.domain.plan.exception.PaymentUpdateBeforeException;
import com.yongy.dotori.domain.plan.repository.PlanRepository;
import com.yongy.dotori.domain.plan.service.PlanService;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class PaymentsScheduler {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PlanRepository planRepository;

    @Scheduled(fixedRate = 30 * 60 * 1000) // 30분(밀리초 단위)
    public void getPayments() throws ParseException {

        List<Plan> activePlanList = planRepository.findAllByPlanState(State.ACTIVE);

        LocalDateTime currentTime = LocalDateTime.now(); // 현재시간
        log.info("현재 시간: {}", currentTime);

        for(Plan plan : activePlanList){
            // 현재 찾으려는 시간이 업데이트한 시간보다 이전이다.
            if(currentTime.isBefore(plan.getUpdatedAt())){
                continue;
            }

            //List<PaymentPodoResDto> paymentResDto = paymentService.getPayments(plan.getUpdatedAt(),plan.getAccount().getAccountSeq());

            // TODO : planSeq가 실행하는 카테고리로 GPT 자동화
        }
    }


}
