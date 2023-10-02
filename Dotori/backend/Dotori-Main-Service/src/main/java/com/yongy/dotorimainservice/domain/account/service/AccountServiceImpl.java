package com.yongy.dotorimainservice.domain.account.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yongy.dotorimainservice.domain.account.dto.AccountDTO;
import com.yongy.dotorimainservice.domain.account.dto.BodyDataDTO;
import com.yongy.dotorimainservice.domain.account.dto.communication.AccountNumberTitleReqDto;
import com.yongy.dotorimainservice.domain.account.dto.communication.AccountReqDto;
import com.yongy.dotorimainservice.domain.account.entity.Account;
import com.yongy.dotorimainservice.domain.account.repository.AccountRepository;
import com.yongy.dotorimainservice.domain.bank.entity.Bank;
import com.yongy.dotorimainservice.domain.bank.repository.BankRepository;

import com.yongy.dotorimainservice.global.redis.repository.BankAccessTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private final BankAccessTokenRepository bankAccessTokenRepository;



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

    @Override
    public BigDecimal getBalance(Long accountSeq) throws JsonProcessingException {
        Account account = accountRepository.findByAccountSeqAndDeleteAtIsNull(accountSeq);
        Bank bankInfo = bankRepository.findByBankSeq(account.getBank().getBankSeq());

        String accessToken = bankAccessTokenRepository.findByBankName(bankInfo.getBankName()).getToken();// 은행 accessToken 가져오기

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=utf-8");

        httpHeaders.add("Authorization","Bearer " + accessToken);

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



    // NOTE : 사용자의 계좌 모두 삭제하기
    public void removeUserAccounts(Long userSeq){
        List<Account> accountList = accountRepository.findAllByUserSeqAndDeleteAtIsNull(userSeq);
        for(Account account : accountList){
            account.setDeleteAt(LocalDateTime.now());
        }
        accountRepository.saveAll(accountList);
    }

    // NOTE : 존재하는 계좌번호인지 확인하기
    public Account getUserAccount(String accountNumber){
        return accountRepository.findByAccountNumberAndDeleteAtIsNull(accountNumber);
    }

    public void saveAccount(AccountReqDto accountReqDto){
        Account account = Account.builder()
                .accountNumber(accountReqDto.getAccountNumber())
                .userSeq(accountReqDto.getUserSeq())
                .bank(bankRepository.findByBankSeq(accountReqDto.getBankSeq()))
                .fintechCode(accountReqDto.getFintechCode()).build();
        accountRepository.save(account);
    }

    public void saveAccountTitle(AccountNumberTitleReqDto accountNumberTitleReqDto){
        Account account = accountRepository.findByAccountNumberAndDeleteAtIsNull(accountNumberTitleReqDto.getAccountNumber());
        account.setAccountTitle(accountNumberTitleReqDto.getAccountTitle());
        accountRepository.save(account);
    }

    public String getAccountTitle(Long accountSeq){
        Account account = accountRepository.findByAccountSeqAndDeleteAtIsNull(accountSeq);
        return account.getAccountTitle();
    }


}
