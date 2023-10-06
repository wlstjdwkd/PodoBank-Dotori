package com.yongy.dotori.global.common;

import com.yongy.dotori.domain.bank.entity.Bank;
import com.yongy.dotori.domain.bank.repository.BankRepository;
import com.yongy.dotori.global.redis.entity.BankAccessToken;
import com.yongy.dotori.global.redis.entity.BankRefreshToken;
import com.yongy.dotori.global.redis.repository.BankAccessTokenRepository;
import com.yongy.dotori.global.redis.repository.BankRefreshTokenRepository;
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

    private static Bank bankInfo;


    public void podoBankLogin(){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json;charset=utf-8");
            log.info(bankInfo.getBankId()+"/"+bankInfo.getBankPwd());
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

            log.info("1- accessToken : "+ accessToken);
            log.info("2- refreshToken : "+ refreshToken);

            bankAccessTokenRepository.save(BankAccessToken.of(bankInfo.getBankName(), accessToken));
            bankRefreshTokenRepository.save(BankRefreshToken.of(bankInfo.getBankName(), refreshToken));
        } catch (ParseException e) {
            throw new IllegalArgumentException("포도뱅크에 로그인할 수 없음");
        }
    }


    // NOTE : accessToken이나 refreshToken을 세팅한다.(없으면 podoBankLogin을 호출해서 새로 발급해서 세팅함)
    public String getConnectionToken(Long bankSeq){
        bankInfo = bankRepository.findByBankSeq(bankSeq);

        log.info(bankInfo.getBankName()+"--1");

        Optional<BankAccessToken> bankAccessToken = bankAccessTokenRepository.findById(bankInfo.getBankName());

        Optional<BankRefreshToken> bankRefreshToken = bankRefreshTokenRepository.findById(bankInfo.getBankName());

        log.info("1- accessToken : "+ bankAccessToken);
        log.info("2- refreshToken : "+ bankRefreshToken);


        String useToken = null;


        // this.podoBankLogin(); // accessToken, refreshToken 재발급

        if(bankAccessToken.isEmpty()){
            if(bankRefreshToken.isEmpty()){
                this.podoBankLogin(); // NOTE : accessToken, refreshToken 재발급
                log.info("--1--");
                useToken = bankAccessTokenRepository.findById(bankInfo.getBankName()).get().getToken();
            }else{
                log.info("--2--");
                useToken = bankRefreshToken.get().getToken();
            }
        }else{
            log.info("--3--");
            useToken = bankAccessToken.get().getToken();
        }

        return useToken;
    }

}
