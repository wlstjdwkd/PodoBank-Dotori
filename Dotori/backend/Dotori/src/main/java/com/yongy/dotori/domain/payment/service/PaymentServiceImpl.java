package com.yongy.dotori.domain.payment.service;

import com.yongy.dotori.domain.account.entity.Account;
import com.yongy.dotori.domain.account.repository.AccountRepository;
import com.yongy.dotori.domain.bank.entity.Bank;
import com.yongy.dotori.domain.bank.repository.BankRepository;
import com.yongy.dotori.domain.payment.dto.response.PaymentPodoResDto;
import com.yongy.dotori.domain.payment.repository.PaymentRepository;
import com.yongy.dotori.domain.plan.service.PlanService;
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

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // NOTE : 시작날짜, 계좌seq
    public List<PaymentPodoResDto> getPayments(LocalDateTime updateTime, Long accountSeq) throws ParseException {
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

        if(response.getStatusCode().toString().split(" ")[0].equals("200")){
            JSONParser jsonParser = new JSONParser();
            JSONArray jsonArray = (JSONArray) jsonParser.parse(response.getBody());

            PaymentPodoResDto paymentPodoResDto;
            String paymentType = null;
            for(Object obj : jsonArray){
                if(obj instanceof JSONObject){
                    JSONObject jsonObject = (JSONObject) obj;

                    paymentType = jsonObject.get("transactionType").toString();

                    // 출금
                    if(paymentType.equals("WITHDRAWAL")){
                        paymentPodoResDto = PaymentPodoResDto.builder()
                                .transactionAt(jsonObject.get("transactionAt").toString())
                                .amount(new BigDecimal(jsonObject.get("amount").toString()))
                                .balanceAfter(new BigDecimal(jsonObject.get("balanceAfter").toString()))
                                .content(jsonObject.get("content").toString()).build();
                        paymentList.add(paymentPodoResDto);
                    }
                }
            }
        }


        return paymentList;

    }

}
