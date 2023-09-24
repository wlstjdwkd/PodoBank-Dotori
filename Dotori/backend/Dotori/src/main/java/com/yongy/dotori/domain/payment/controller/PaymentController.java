package com.yongy.dotori.domain.payment.controller;

import com.yongy.dotori.domain.payment.dto.request.PaymentPodoReqDto;
import com.yongy.dotori.domain.payment.dto.response.PaymentPodoResDto;
import com.yongy.dotori.domain.payment.repository.PaymentRepository;
import com.yongy.dotori.domain.payment.service.PaymentService;
import com.yongy.dotori.domain.plan.entity.Plan;
import com.yongy.dotori.domain.plan.exception.PaymentUpdateBeforeException;
import com.yongy.dotori.domain.plan.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RequestMapping("/v1/payment")
@RequiredArgsConstructor
@RestController
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PlanRepository planRepository;

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // NOTE : 결제내역 가져오기
    @PostMapping("/listAll")
    public ResponseEntity<List<PaymentPodoResDto>> updatePayments(@RequestBody PaymentPodoReqDto paymentPodoReqDto) throws ParseException {

        Plan plan = planRepository.findByPlanSeq(paymentPodoReqDto.getPlanSeq());

        LocalDateTime updateTime = LocalDateTime.parse(paymentPodoReqDto.getStartAt(), formatter); // 오늘의 날짜

        // 현재 찾으려는 시간이 업데이트한 시간보다 이전이다. plan.getUpdatedAt() != null &&
        if(updateTime.isBefore(plan.getUpdatedAt())){
            throw new PaymentUpdateBeforeException("이미 이전에 업데이트를 했음");
        }

        // 업데이트를 하려는 날짜, 계좌의 sequence
        List<PaymentPodoResDto> paymenetList;
        paymenetList = paymentService.getPayments(plan.getUpdatedAt(), paymentPodoReqDto.getAccountSeq());

        // 업데이트한 날짜 저장
        plan.setUpdatedAt(updateTime);
        planRepository.save(plan);


        return ResponseEntity.ok(paymenetList);
    }
}
