package com.yongy.dotori.domain.userAuth.service;

import com.yongy.dotori.domain.bank.entity.Bank;
import com.yongy.dotori.domain.bank.repository.BankRepository;
import com.yongy.dotori.domain.userAuth.dto.request.UserAccountCodeDto;
import com.yongy.dotori.domain.userAuth.dto.request.UserAccountDto;
import com.yongy.dotori.global.common.BaseResponseBody;
import com.yongy.dotori.global.email.EmailUtil;
import com.yongy.dotori.global.redis.entity.BankAccessToken;
import com.yongy.dotori.global.redis.entity.BankRefreshToken;
import com.yongy.dotori.global.redis.entity.FintechToken;
import com.yongy.dotori.global.redis.entity.PersonalAuth;
import com.yongy.dotori.global.redis.repository.BankAccessTokenRepository;
import com.yongy.dotori.global.redis.repository.BankRefreshTokenRepository;
import com.yongy.dotori.global.redis.repository.FintechTokenRepository;
import com.yongy.dotori.global.redis.repository.PersonalAuthRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserAuthService {
    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private BankRepository bankRepository;

    // NOTE : RedisDB
    @Autowired
    private PersonalAuthRepository personalAuthRepository;

    @Autowired
    private BankAccessTokenRepository bankAccessTokenRepository;

    @Autowired
    private BankRefreshTokenRepository bankRefreshTokenRepository;

    @Autowired
    private FintechTokenRepository fintechTokenRepository;

    private static Bank bankInfo;


    // NOTE : 1원인증
    public void ownCert(String id){
        Random random = new Random();
        String authCode = String.valueOf(random.nextInt(888888) + 111111); // 11111 ~ 99999의 랜덤한 숫자
        sendOwnAuthCode(id, authCode);
    }

    public void sendOwnAuthCode(String id, String authCode){
        emailUtil.setSubject("도토리 1원인증 코드");
        emailUtil.setPrefix("1원인증을 위한 인증번호는 ");
        emailUtil.sendEmailAuthCode(id, authCode);
        personalAuthRepository.save(PersonalAuth.of(authCode, id));
    }

    public String getOwnAuthId(String authCode) {
        Optional<PersonalAuth> personalAuth = personalAuthRepository.findById(authCode);
        if (personalAuth != null)
            return personalAuth.get().getId();
        return null;
    }
    public void deleteOwnAuthCode(String authCode){ personalAuthRepository.deleteById(authCode);}

    // NOTE: 사용자의 access, refreshToken 가져오기 headers.add("Content-Type", "application/json;charset=utf-8");
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
            throw new RuntimeException(e);
        }
    }

    // NOTE : accessToken이나 refreshToken을 세팅한다.(없으면 podoBankLogin을 호출해서 새로 발급해서 세팅함)
    public String getConnectionToken(Long bankSeq){
        Optional<BankAccessToken> dotoriAccessToken = bankAccessTokenRepository.findById("accessToken");
        Optional<BankRefreshToken> dotoriRefreshToken = bankRefreshTokenRepository.findById("refreshToken");

        String useToken = null;

        if(dotoriAccessToken.isEmpty()){
            if(dotoriRefreshToken.isEmpty()){
                bankInfo = bankRepository.findByBankSeq(bankSeq);
                this.podoBankLogin(bankInfo); // accessToken, refreshToken 재발급
                useToken = bankAccessTokenRepository.findById("accessToken").get().getToken();
            }else{
                useToken = dotoriRefreshToken.get().getToken();
            }
        }else{
            useToken = dotoriAccessToken.get().getToken();
        }
        return useToken;
    }

    // NOTE : 1원인증
    public ResponseEntity<? extends BaseResponseBody>sendAccountInfo(UserAccountDto userAccountDto) throws ParseException {
        String useToken = this.getConnectionToken(userAccountDto.getBankSeq());

        // 은행의 정보
        if(bankInfo == null)
            bankInfo = bankRepository.findByBankSeq(userAccountDto.getBankSeq());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + useToken);
        headers.add("Content-Type", "application/json;charset=utf-8");

        Map<String, String> bodyData = new HashMap<>();
        bodyData.put("serviceCode", bankInfo.getServiceCode());
        bodyData.put("accountNumber", userAccountDto.getAccountNumber()); // accountNumber


        HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(bodyData, headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(
                    bankInfo.getBankUrl() + "/api/v1/fintech/oneCentVerification",
                    HttpMethod.POST,
                    httpEntity,
                    String.class
        );

        String responseCode = response.getStatusCode().toString().split(" ")[0];


        if(responseCode.equals("200"))
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(Integer.parseInt(responseCode), "success"));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of(Integer.parseInt(responseCode), "fail"));
    }
    // NOTE : 1원 인증의 인증코드를 전송함
    public ResponseEntity<? extends BaseResponseBody>checkAccountAuthCode(UserAccountCodeDto userAccountCodeDto) throws ParseException {
        String useToken = this.getConnectionToken(userAccountCodeDto.getBankSeq());

        // 은행의 정보
        if(bankInfo == null)
            bankInfo = bankRepository.findByBankSeq(userAccountCodeDto.getBankSeq());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + useToken);
        headers.add("Content-Type", "application/json;charset=utf-8");

        Map<String, String> bodyData = new HashMap<>();
        bodyData.put("serviceCode", bankInfo.getServiceCode());
        bodyData.put("accountNumber", userAccountCodeDto.getAccountNumber());
        bodyData.put("verificationCode", "도토리"+userAccountCodeDto.getVerificationCode());

        HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(bodyData, headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(
                bankInfo.getBankUrl() + "/api/v1/fintech/oneCentVerification/check",
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        String responseCode = response.getStatusCode().toString().split(" ")[0];

        if(responseCode.equals("200")){
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject)jsonParser.parse(response.getBody());

            String fintechCode = jsonObject.get("fintechCode").toString();

            fintechTokenRepository.save(FintechToken.of(userAccountCodeDto.getAccountNumber(), fintechCode));

            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(Integer.parseInt(responseCode), "success"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of(Integer.parseInt(responseCode), "fail"));
    }

}
