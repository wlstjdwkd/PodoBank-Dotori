package com.yongy.dotorimainservice.domain.account.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yongy.dotorimainservice.domain.account.dto.AccountDTO;
import com.yongy.dotorimainservice.domain.account.dto.BodyDataDTO;
import com.yongy.dotorimainservice.domain.account.entity.Account;
import com.yongy.dotorimainservice.domain.account.repository.AccountRepository;
import com.yongy.dotorimainservice.domain.bank.entity.Bank;
import com.yongy.dotorimainservice.domain.bank.repository.BankRepository;
import com.yongy.dotorimainservice.global.communication.ApiForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class AccountServiceImpl implements AccountService{
    private final BankRepository bankRepository;

    // private final UserAuthService userAuthService; // TODO : find
    private final AccountRepository accountRepository;

    private ApiForm apiForm;


    @Override
    public List<AccountDTO> findAllAccount() throws JsonProcessingException {
            // TODO : find
//        Map<String, String> bodyData = new HashMap<>();
//        bodyData.put("id", accountRepositor)
//
//        ResponseEntity<String> response = apiForm.sendPostData("/communication/userInfo"
//        ,)
//
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();



        // TODO : find
        // List<Account> accounts = accountRepository.findAllByUserUserSeqAndDeleteAtIsNull(user.getUserSeq());
        List<AccountDTO> result = new ArrayList<>();

        // TODO : find
//        for(Account account : accounts){
//            result.add(AccountDTO.builder()
//                    .accountSeq(account.getAccountSeq())
//                    .accountTitle(account.getAccountTitle())
//                    .currentBalance(getBalance(account.getAccountSeq()))
//                    .build());
//        }

        // return result;

        return null;
    }

    public BigDecimal getBalance(Long accountSeq) throws JsonProcessingException {
        Account account = accountRepository.findByAccountSeqAndDeleteAtIsNull(accountSeq);
        Bank bankInfo = bankRepository.findByBankSeq(account.getBank().getBankSeq());

        // TODO : find
        //String accessToken = userAuthService.getConnectionToken(bankInfo.getBankSeq()); // 은행 accessToken 가져오기

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=utf-8");

        // TODO : find
        // httpHeaders.add("Authorization","Bearer " + accessToken);

        Map<String, String> bodyData = new HashMap<>();
        bodyData.put("serviceCode", bankInfo.getServiceCode());
        bodyData.put("fintechCode", account.getFintechCode());

        HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(bodyData, httpHeaders);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(
                bankInfo.getBankUrl()+"/api/v1/fintech/balance",
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        String responseCode = response.getStatusCode().toString().split(" ")[0];
        String responseContent = response.getBody();

        if(responseCode.equals("200")){
            ObjectMapper objectMapper = new ObjectMapper();
            BodyDataDTO data = objectMapper.readValue(responseContent,BodyDataDTO.class);
            return data.getBalance();
        }

        throw new IllegalArgumentException("계좌 정보를 불러오는데 실패했습니다.");
    }


}
