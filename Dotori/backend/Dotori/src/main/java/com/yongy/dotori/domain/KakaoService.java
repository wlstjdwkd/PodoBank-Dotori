package com.yongy.dotori.domain;

import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class KakaoService {

    @Value("${kakao.client.id}")
    private String KAKAO_CLIENT_ID;

    @Value("${kakao.client.secret}")
    private String KAKAO_CLIENT_SECRET;

    @Value("${kakao.redirect.url}")
    private String KAKAO_REDIRECT_URL;

    private final static String KAKAO_AUTH_URI = "https://kauth.kakao.com";
    private final static String KAKAO_API_URI = "https://kapi.kakao.com";

    public String getKakaoLogin() {
        return KAKAO_AUTH_URI + "/oauth/authorize"
                + "?client_id=" + KAKAO_CLIENT_ID
                + "&redirect_uri=" + KAKAO_REDIRECT_URL
                + "&response_type=code";
    }

    public KakaoDTO getKakaoInfo(String code) throws Exception{
        if(code == null) throw new Exception("인증 코드를 가져오는데 실패함");

        String accessToken = "";
        String refreshToken = "";

        try{
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            // body에 담을 데이터를 저장함
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", KAKAO_CLIENT_ID);
            params.add("client_secret", KAKAO_CLIENT_SECRET); // 필수 X => 보안을 위해
            params.add("code", code);
            params.add("redirect_uri", KAKAO_REDIRECT_URL);

            RestTemplate restTemplate = new RestTemplate();

            // request에 header, data를 저장한다.
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

            // HTTP 요청을 보내고 응답을 받음(통신)
            ResponseEntity<String> response = restTemplate.exchange(
                    KAKAO_AUTH_URI +"/oauth/token",
                    HttpMethod.POST,
                    httpEntity,
                    String.class
            );

            // response를 key-value로 파싱하기
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());

            accessToken = (String)jsonObj.get("access_token");
            refreshToken = (String)jsonObj.get("refresh_token");

            log.info("access_token :  "+ accessToken);
            log.info("refresh_token : "+ refreshToken);

        }catch(Exception e){
            throw new Exception("API 호출에 실패함");
        }

        return getUserInfoFromToken(accessToken);
    }

    private KakaoDTO getUserInfoFromToken(String accessToken) throws Exception{

        // HttpHeader 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpHeader 담기
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
            KAKAO_API_URI + "/v2/user/me",
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        // Response 데이터 파싱
        JSONParser jsonParser = new JSONParser();

        log.info(jsonParser.toString());

        JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());
        JSONObject account = (JSONObject) jsonObj.get("kakao_account");
        JSONObject profile = (JSONObject) account.get("profile");

        long id = (long) jsonObj.get("id");
        String email = String.valueOf(account.get("email"));
        String nickname = String.valueOf(profile.get("nickname"));

        log.info("info : "+ id+","+email+","+nickname);

        KakaoDTO kakaoDTO = KakaoDTO.builder()
                .id(id)
                .email(email)
                .nickname(nickname).build();
        log.info("info : "+ kakaoDTO.getId()+","+kakaoDTO.getEmail()+","+kakaoDTO.getNickname());

        return kakaoDTO;
    }


}