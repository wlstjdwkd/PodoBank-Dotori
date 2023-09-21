package com.yongy.dotori.domain.account.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yongy.dotori.domain.account.dto.AccountDTO;
import com.yongy.dotori.domain.account.dto.BodyDataDTO;
import com.yongy.dotori.domain.account.entity.Account;
import com.yongy.dotori.domain.bank.entity.Bank;
import com.yongy.dotori.domain.bank.repository.BankRepository;
import com.yongy.dotori.domain.user.entity.User;
import com.yongy.dotori.domain.userAuth.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.hibernate.type.descriptor.java.ObjectJavaType;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService{
    private final BankRepository bankRepository;
    private final UserAuthService userAuthService;

    @Override
    public List<AccountDTO> findAllAccount() throws JsonProcessingException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Account> accounts = user.getAccountList();
        List<AccountDTO> result = new ArrayList<>();

        for(Account account : accounts){
            result.add(AccountDTO.builder()
                    .accountSeq(account.getAccountSeq())
                    .accountTitle(account.getAccountTitle())
                    .currentBalance(getBalance(account.getAccountSeq()))
                    .build());
        }

        return result;
    }

    public BigDecimal getBalance(Long accountSeq) throws JsonProcessingException {
        Bank bankInfo = bankRepository.findByAccountAccountSeq(accountSeq);
        String accessToken = userAuthService.getConnectionToken(bankInfo.getBankSeq());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=utf-8");
        httpHeaders.add("Authorization","Bearer " + accessToken);

        Map<String, String> bodyData = new HashMap<>();
        bodyData.put("serviceCode", "");
        bodyData.put("fintechCode", "");

        HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(bodyData, httpHeaders);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(
                bankInfo.getBankUrl()+"/api/v1/fintech/balance",
                HttpMethod.GET,
                httpEntity,
                String.class
        );

        String responseCode = response.getStatusCode().toString().split(" ")[0];
        String responseContent = response.getBody().toString();

        if(responseCode.equals("200")){
            ObjectMapper objectMapper = new ObjectMapper();
            BodyDataDTO data = objectMapper.readValue(responseContent,BodyDataDTO.class);

            return data.getBalance();
        }

        return null;
    }
}
