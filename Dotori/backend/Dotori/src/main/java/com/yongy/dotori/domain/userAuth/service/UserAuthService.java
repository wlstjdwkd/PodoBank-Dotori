package com.yongy.dotori.domain.userAuth.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yongy.dotori.domain.bank.entity.Bank;
import com.yongy.dotori.domain.bank.repository.BankRepository;
import com.yongy.dotori.domain.userAuth.dto.request.UserAccountDto;
import com.yongy.dotori.global.common.BaseResponseBody;
import com.yongy.dotori.global.email.EmailUtil;
import com.yongy.dotori.global.redis.entity.DotoriToken;
import com.yongy.dotori.global.redis.entity.PersonalAuth;
import com.yongy.dotori.global.redis.repository.DotoriTokenRepository;
import com.yongy.dotori.global.redis.repository.PersonalAuthRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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
    private DotoriTokenRepository dotoriTokenRepository;


    private long accessTokenExp = 1000L * 60 * 3; // 3분

    private long refreshTokenExp = 1000L * 60 * 60 * 24 * 6; // 6일


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

            MultiValueMap<String, String> parameter = new LinkedMultiValueMap<>();
            parameter.add("email", bankInfo.getBankId());
            parameter.add("password", bankInfo.getBankPwd());

            String jsonRequestBody = "{\"email\":\"dotori@dotori.com\",\"password\":\"qwer1234!\"}";

            HttpEntity<String> httpEntity = new HttpEntity<>(jsonRequestBody, headers);
            log.info(httpEntity.getBody().toString());

            RestTemplate restTemplate = new RestTemplate();

            log.info("come!!");
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

            dotoriTokenRepository.save(DotoriToken.of("accessToken", accessToken,accessTokenExp));

            dotoriTokenRepository.save(DotoriToken.of("refreshToken", refreshToken,refreshTokenExp));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<? extends BaseResponseBody>sendAccountInfo(UserAccountDto userAccountDto) throws ParseException {
        Optional<DotoriToken> dotoriAccessToken = dotoriTokenRepository.findById("accessToken");
        Optional<DotoriToken> dotoriRefreshToken = dotoriTokenRepository.findById("refreshToken");

        String useToken = null;

        Bank bankInfo = null;

        log.info("log : "+dotoriAccessToken+"////"+dotoriRefreshToken);


        if(dotoriAccessToken.isEmpty()){
            if(dotoriRefreshToken.isEmpty()){
                bankInfo = bankRepository.findByBankSeq(userAccountDto.getBankSeq());

                this.podoBankLogin(bankInfo); // accessToken, refreshToken 재발급
                useToken = dotoriTokenRepository.findById("refreshToken").get().getToken();
            }else{
                useToken = dotoriRefreshToken.get().getToken();
            }
        }else{
            useToken = dotoriAccessToken.get().getToken();
        }

        // NOTE : 사용자가 선택한 정보를 포도은행에 보낸다.
        // 은행의 정보
        if(bankInfo == null)
            bankInfo = bankRepository.findByBankSeq(userAccountDto.getBankSeq());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + useToken);
        headers.add("Content-Type", "application/json;charset=utf-8");

        MultiValueMap<String, String> parameter = new LinkedMultiValueMap<>();
        parameter.add("serviceCode", bankInfo.getServiceCode());
        parameter.add("account", userAccountDto.getAccountNumber());

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(parameter, headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(
                    bankInfo.getBankUrl(),
                    HttpMethod.POST,
                    httpEntity,
                    String.class
        );

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(response.getBody());

        String responseCode = null;
        String responseMessage = "";


        for(Object key : jsonObject.keySet()){
            responseCode = (String) key;
            responseMessage = jsonObject.get(responseCode).toString();
            break;
        }

        if(responseCode.equals("200"))
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(Integer.parseInt(responseCode), responseMessage));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of(Integer.parseInt(responseCode), responseMessage));
    }

}
