package com.yongy.dotori.domain.account.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yongy.dotori.domain.account.dto.AccountDTO;
import com.yongy.dotori.domain.account.dto.BodyDataDTO;
import com.yongy.dotori.domain.account.entity.Account;
import com.yongy.dotori.domain.account.repository.AccountRepository;
import com.yongy.dotori.domain.bank.entity.Bank;
import com.yongy.dotori.domain.bank.repository.BankRepository;
import com.yongy.dotori.domain.user.entity.User;
import com.yongy.dotori.domain.userAuth.service.UserAuthService;
import com.yongy.dotori.global.redis.repository.FintechTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService{
    private final BankRepository bankRepository;
    private final UserAuthService userAuthService;
    private final AccountRepository accountRepository;
    private final FintechTokenRepository fintechTokenRepository;

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
        Account account = accountRepository.findByAccountSeq(accountSeq);
        Bank bankInfo = bankRepository.findByBankSeq(account.getBank().getBankSeq());
        String accessToken = userAuthService.getConnectionToken(bankInfo.getBankSeq()); // 은행 accessToken 가져오기

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=utf-8");
        httpHeaders.add("Authorization","Bearer " + accessToken);

        Map<String, String> bodyData = new HashMap<>();
        bodyData.put("serviceCode", accessToken);
        bodyData.put("fintechCode", fintechTokenRepository.findByAccountNumber(account.getAccountNumber()).getFintechCode());

        HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(bodyData, httpHeaders);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(
                bankInfo.getBankUrl()+"/api/v1/fintech/balance",
                HttpMethod.GET,
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
