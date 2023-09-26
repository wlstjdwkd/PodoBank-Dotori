package com.yongy.dotori.domain.userAuth.service;

import com.yongy.dotori.domain.account.entity.Account;
import com.yongy.dotori.domain.account.repository.AccountRepository;
import com.yongy.dotori.domain.bank.entity.Bank;
import com.yongy.dotori.domain.bank.repository.BankRepository;
import com.yongy.dotori.domain.user.entity.User;
import com.yongy.dotori.domain.userAuth.dto.request.UserAccountCodeDto;
import com.yongy.dotori.domain.userAuth.dto.request.UserAccountDto;
import com.yongy.dotori.global.common.BaseResponseBody;
import com.yongy.dotori.global.email.EmailUtil;
import com.yongy.dotori.global.redis.entity.BankAccessToken;
import com.yongy.dotori.global.redis.entity.BankRefreshToken;
// import com.yongy.dotori.global.redis.entity.FintechToken;
import com.yongy.dotori.global.redis.entity.PersonalAuth;
import com.yongy.dotori.global.redis.repository.BankAccessTokenRepository;
import com.yongy.dotori.global.redis.repository.BankRefreshTokenRepository;
// import com.yongy.dotori.global.redis.repository.FintechTokenRepository;
import com.yongy.dotori.global.redis.repository.PersonalAuthRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PersonalAuthRepository personalAuthRepository;

    @Autowired
    private BankAccessTokenRepository bankAccessTokenRepository;

    @Autowired
    private BankRefreshTokenRepository bankRefreshTokenRepository;

    private static Bank bankInfo;

    // NOTE : PersonalAuth 반환
    public PersonalAuth getPersonalAuth(String authCode) {
        return personalAuthRepository.findByAuthCode(authCode);
    }

    // NOTE : PersonalAuth 삭제
    public void deletePersonalAuth(String email){
        personalAuthRepository.deleteById(email);
    }

    // NOTE : 사용자의 계좌 반환
    public Account getUserAccount(String accountNumber){
        return accountRepository.findByAccountNumberAndDeleteAtIsNull(accountNumber);
    }

    // NOTE : 사용자의 계좌 저장
    public void saveUserAccount(Account account){
        accountRepository.save(account);
    }

    // NOTE : [본인인증]이메일 인증코드를 생성한다.
    public void emailCertification(String id){
        Random random = new Random();
        String authCode = String.valueOf(random.nextInt(888888) + 111111); // 11111 ~ 99999의 랜덤한 숫자
        sendEmailCertification(id, authCode);
    }

    // NOTE : [본인인증]인증코드를 사용자 이메일로 전송한다.
    public void sendEmailCertification(String id, String authCode){
        emailUtil.setSubject("도토리 1원인증 코드");
        emailUtil.setPrefix("1원인증을 위한 인증번호는 ");
        emailUtil.sendEmailAuthCode(id, authCode);
        personalAuthRepository.save(PersonalAuth.of(id, authCode));
    }


    // NOTE: 사용자의 access, refreshToken 가져오기
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


        if(bankAccessToken.isEmpty()){
            if(bankRefreshToken.isEmpty()){
                this.podoBankLogin(); // accessToken, refreshToken 재발급
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

    // NOTE : 1원 인증코드를 보낸다.
    public String sendAccountAuthCode(UserAccountDto userAccountDto){
        String useToken = this.getConnectionToken(userAccountDto.getBankSeq());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + useToken);
        headers.add("Content-Type", "application/json;charset=utf-8");

        Map<String, String> bodyData = new HashMap<>();
        bodyData.put("serviceCode", bankInfo.getServiceCode());
        bodyData.put("accountNumber", userAccountDto.getAccountNumber());

        HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(bodyData, headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(
                    bankInfo.getBankUrl() + "/api/v1/fintech/oneCentVerification",
                    HttpMethod.POST,
                    httpEntity,
                    String.class
        );

        String responseCode = response.getStatusCode().toString().split(" ")[0]; // 200

        return responseCode;
    }

    // NOTE : 1원 인증의 인증코드를 전송함
    public ResponseEntity<Void>checkAccountAuthCode(UserAccountCodeDto userAccountCodeDto) throws ParseException {
        String useToken = this.getConnectionToken(userAccountCodeDto.getBankSeq());

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

            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Account account = Account.builder()
                    .accountNumber(userAccountCodeDto.getAccountNumber())
                    .user(user)
                    .bank(bankInfo)
                    .fintechCode(fintechCode) // NOTE : fintech 코드를 계좌에 등록한다.
                    .build();

            accountRepository.save(account);

            return ResponseEntity.ok().build();
        }
        throw new IllegalArgumentException("1원 인증 실패");
    }
}
