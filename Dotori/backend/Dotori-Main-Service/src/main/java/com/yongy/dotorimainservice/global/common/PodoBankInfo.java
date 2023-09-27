package com.yongy.dotorimainservice.global.common;


import com.yongy.dotoripurposeservice.domain.bank.entity.Bank;
import com.yongy.dotoripurposeservice.domain.bank.repository.BankRepository;
import com.yongy.dotoripurposeservice.global.redis.entity.BankAccessToken;
import com.yongy.dotoripurposeservice.global.redis.entity.BankRefreshToken;
import com.yongy.dotoripurposeservice.global.redis.repository.BankAccessTokenRepository;
import com.yongy.dotoripurposeservice.global.redis.repository.BankRefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PodoBankInfo {

    @Autowired
    private BankAccessTokenRepository bankAccessTokenRepository;

    @Autowired
    private BankRefreshTokenRepository bankRefreshTokenRepository;

    @Autowired
    private BankRepository bankRepository;


    public void podoBankLogin(Bank bankInfo){
        try{

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json;charset=utf-8");

            Map<String, String> bodyData = new HashMap<>();
            bodyData.put("email", bankInfo.getBankId());
            bodyData.put("password", bankInfo.getBankPwd());

            HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(bodyData, headers);

            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<String> response = restTemplate.exchange(
                    bankInfo.getBankUrl()+"/api/v1/user/login",
                    HttpMethod.POST,
                    httpEntity,
                    String.class

            );

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(response.getBody());

            String accessToken = (String) jsonObject.get("accessToken");
            String refreshToken = (String) jsonObject.get("refreshToken");

            bankAccessTokenRepository.save(BankAccessToken.of("accessToken", accessToken));
            bankRefreshTokenRepository.save(BankRefreshToken.of("refreshToken", refreshToken));
        } catch (ParseException e) {
            throw new IllegalArgumentException("포도뱅크에 로그인할 수 없음");
        }
    }


    // NOTE : accessToken이나 refreshToken을 세팅한다.(없으면 podoBankLogin을 호출해서 새로 발급해서 세팅함)
    public String getConnectionToken(Long bankSeq){
        Optional<BankAccessToken> dotoriAccessToken = bankAccessTokenRepository.findById("accessToken");
        Optional<BankRefreshToken> dotoriRefreshToken = bankRefreshTokenRepository.findById("refreshToken");

        String useToken = null;

        Bank bankInfo = null;

        if(dotoriAccessToken.isEmpty()){
            if(dotoriRefreshToken.isEmpty()){
                log.info("accessToken, refreshToken 재발급");
                bankInfo = bankRepository.findByBankSeq(bankSeq);
                this.podoBankLogin(bankInfo); // accessToken, refreshToken 재발급
                useToken = bankAccessTokenRepository.findById("accessToken").get().getToken();
            }else{
                log.info("refreshToken 사용");
                useToken = dotoriRefreshToken.get().getToken();
            }
        }else{
            log.info("accessToken 사용");
            useToken = dotoriAccessToken.get().getToken();
        }
        return useToken;
    }

}
