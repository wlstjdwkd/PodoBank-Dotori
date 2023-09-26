package com.yongy.dotori.domain.payment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yongy.dotori.domain.account.entity.Account;
import com.yongy.dotori.domain.account.repository.AccountRepository;
import com.yongy.dotori.domain.bank.entity.Bank;
import com.yongy.dotori.domain.bank.repository.BankRepository;
import com.yongy.dotori.domain.payment.dto.PaymentDetailDTO;
import com.yongy.dotori.domain.payment.dto.TransactionHistoryDTO;
import com.yongy.dotori.domain.payment.dto.UpdateDataDTO;
import com.yongy.dotori.domain.payment.dto.UpdateUnclassifiedDTO;
import com.yongy.dotori.domain.payment.dto.response.PaymentPodoResDto;
import com.yongy.dotori.domain.payment.entity.Payment;
import com.yongy.dotori.domain.payment.repository.PaymentRepository;
import com.yongy.dotori.domain.plan.service.PlanService;
import com.yongy.dotori.domain.planDetail.entity.PlanDetail;
import com.yongy.dotori.domain.planDetail.repository.PlanDetailRepository;
import com.yongy.dotori.global.common.PodoBankInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private PodoBankInfo podoBankInfo;
    private final PlanDetailRepository planDetailRepository;

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // NOTE : 시작날짜, 계좌seq
    public List<PaymentPodoResDto> getPayments(LocalDateTime updateTime, Long accountSeq) throws ParseException, JsonProcessingException {
        Account account = accountRepository.findByAccountSeqAndDeleteAtIsNull(accountSeq);
        Bank bank = bankRepository.findByBankSeq(account.getBank().getBankSeq());

        String useToken = podoBankInfo.getConnectionToken(bank.getBankSeq());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        headers.add("Authorization", "Bearer " + useToken);

        HashMap<String, String> bodyData = new HashMap<>();
        bodyData.put("serviceCode",bank.getServiceCode());
        bodyData.put("fintechCode",account.getFintechCode());
        bodyData.put("startAt", updateTime.toString());

        HttpEntity<HashMap<String, String>> httpEntity = new HttpEntity<>(bodyData, headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(
                bank.getBankUrl()+"/api/v1/fintech/history",
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        List<PaymentPodoResDto> paymentList = new ArrayList<>();
        log.info("state : "+ response.getStatusCode().toString().split(" ")[0]);

        if(response.getStatusCode().toString().split(" ")[0].equals("200")){
            JSONParser jsonParser = new JSONParser();
            JSONArray jsonArray = (JSONArray) jsonParser.parse(response.getBody());

            ObjectMapper objectMapper = new ObjectMapper();
            List<TransactionHistoryDTO> payments = objectMapper.readValue(jsonArray.toString(), objectMapper.getTypeFactory().constructCollectionType(List.class, TransactionHistoryDTO.class));
            payments = payments.stream().filter(TransactionHistoryDTO -> TransactionHistoryDTO.getTransactionType().equals("WITHDRAWAL")).toList();

            return payments.stream()
                    .map(transactionHistoryDTO -> PaymentPodoResDto.builder()
                            .transactionAt(transactionHistoryDTO.getTransactionAt())
                            .amount(transactionHistoryDTO.getAmount())
                            .balanceAfter(transactionHistoryDTO.getBalanceAfter())
                            .content(transactionHistoryDTO.getContent())
                            .code(null) // 사업자 코드를 설정해야 합니다.
                            .build())
                    .collect(Collectors.toList());

        }


        return paymentList;
    }

    @Override
    public List<PaymentDetailDTO> findAllUnclassified(Long planSeq) {

        // TODO : planSeq에 연결된 planDetail들에 연결된 payment 중에 checked가 false인 것 가져오기
        List<PlanDetail> planDetails = planDetailRepository.findAllByPlanPlanSeq(planSeq);
        List<Payment> unclassifiedData = new ArrayList<>();

        for(PlanDetail planDetail : planDetails){
            unclassifiedData.addAll(paymentRepository.findAllByPlanDetailAndChecked(planDetail, false));
        }

        // 반환할 데이터 형식으로 변환
        List<PaymentDetailDTO> result = new ArrayList<>();
        for(Payment payment : unclassifiedData){
            result.add(PaymentDetailDTO.builder()
                            .planDetailSeq(payment.getPlanDetail().getPlanDetailSeq())
                            .categoryName(payment.getPlanDetail().getCategory().getCategoryTitle())
                            .paymentSeq(payment.getPaymentSeq())
                            .paymentName(payment.getPaymentName())
                            .paymentPrice(payment.getPaymentPrice())
                            .paymentDate(payment.getPaymentDate().format(formatter))
                    .build());
        }
        return result;
    }

    @Override
    public void updateUnclassified(Long planSeq, UpdateUnclassifiedDTO updateUnclassifiedDTO) {

        // TODO : payment의 checked = true로 변경
        // TODO : category_data에 저장
        List<UpdateDataDTO> payments = updateUnclassifiedDTO.getUpdateData();
        List<Payment> result = new ArrayList<>();

        for(UpdateDataDTO data : payments){
            Payment payment = paymentRepository.findByPaymentSeq(data.getPaymentSeq());
            result.add(payment.updateChecked());
        }

        paymentRepository.saveAll(result);


    }
}
