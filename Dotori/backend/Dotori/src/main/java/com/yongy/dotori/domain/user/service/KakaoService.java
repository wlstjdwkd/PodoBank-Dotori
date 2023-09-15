package com.yongy.dotori.domain.user.service;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.yongy.dotori.domain.user.entity.Provider;
import com.yongy.dotori.domain.user.entity.Role;
import com.yongy.dotori.domain.user.entity.User;
import com.yongy.dotori.domain.user.repository.UserRepository;
import com.yongy.dotori.global.common.BaseResponseBody;
import com.yongy.dotori.global.redis.RedisUtil;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Slf4j
@Transactional
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

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserRepository userRepository;
    private String accessToken = "";
    private String refreshToken = "";

    private final long exp = 1000L * 60 * 60;

    // TODO : 인가코드 받기
    public String getKakaoLogin() {
        return KAKAO_AUTH_URI + "/oauth/authorize"
                + "?client_id=" + KAKAO_CLIENT_ID
                + "&redirect_uri=" + KAKAO_REDIRECT_URL
                + "&response_type=code";
    }

    // TODO : 새로운 accessToken, refreshToken을 발급하기
    public ResponseEntity<? extends BaseResponseBody> newTokens(String code){

        if (code == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(404, "인증코드가 존재하지 않습니다."));

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

            // response를 파싱하기
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());

            accessToken = (String)jsonObj.get("access_token");
            refreshToken = (String)jsonObj.get("refresh_token");

            log.info("access_token :  "+ accessToken);
            log.info("refresh_token : "+ refreshToken);

            User user = (User) getUserInfo(accessToken).getBody().getData();

            // RefreshToken이 없는 경우(시간이 만료되었거나, 처음 들어오는 사용자)
            if(redisUtil.getData(user.getId()) == null){
                // DB에 사용자의 정보가 없는 경우
                if(userRepository.findById(user.getId()) == null){
                    userRepository.save(user); // DB에 사용자 저장
                }
            }

            redisUtil.setDataExpire(user.getId(), refreshToken,exp * 24);
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, accessToken));
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of(404, "인증코드가 유효하지 않습니다."));
        }
    }

    // TODO : accessToken으로 사용자 정보 가져오기
    public ResponseEntity<? extends BaseResponseBody> getUserInfo(String accessToken) throws Exception{
        // 유효한 accessToken인지 검사함
        if(validateToken(accessToken).getStatusCode() == HttpStatus.OK){
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

            User user = User.builder()
                    .id(String.valueOf(account.get("email")))
                    .userName(String.valueOf(profile.get("nickname")))
                    .role(Role.USER)
                    .authProvider(Provider.KAKAO)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, user));
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of(404, "accessToken이 유효하지 않습니다."));
        }
    }


    // TODO : 토큰의 유효성 검사
    public ResponseEntity<? extends BaseResponseBody> validateToken(String accessToken){
        // HttpHeader 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);

        try{
            ResponseEntity<String> response = restTemplate.exchange(
                    KAKAO_API_URI + "/v1/user/access_token_info",
                    HttpMethod.GET,
                    httpEntity,
                    String.class
            );
            System.out.println(response.getHeaders());

            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "유효한 토큰입니다."));
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of(401, "유효하지 않은 앱키나 액세스 토큰입니다."));
        }
    }

    // TODO : 토큰 갱신하기 (FE랑 상의하기)
    public ResponseEntity<? extends BaseResponseBody> tokenUpdate(String id){
        String refreshToken = redisUtil.getData(id);

        if(refreshToken == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(404, "refreshToken이 존재하지 않습니다."));

        try{
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "refresh_token");
            params.add("client_id", KAKAO_CLIENT_ID);
            params.add("refresh_token", refreshToken);

            RestTemplate restTemplate = new RestTemplate();

            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    KAKAO_AUTH_URI + "/oauth/token",
                    HttpMethod.POST,
                    httpEntity,
                    String.class
            );

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());

            if(jsonObj.containsKey("access_token")){
                // 기존 refresh_Token의 유효기간이 1개월 미만인 경우에만 갱신한다.
                accessToken = (String) jsonObj.get("access_token");
                refreshToken = (String) jsonObj.get("refresh_token");
                redisUtil.setDataExpire(id, refreshToken, exp * 24);

                return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, accessToken));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(404, "refresh_Token의 유효기간이 1개월 미만인 경우에만 갱신한다."));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(404, "토큰을 갱신할 수 없습니다."));
        }
    }


}