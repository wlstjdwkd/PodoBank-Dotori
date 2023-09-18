package com.yongy.dotori.global.security.service;



import com.yongy.dotori.domain.user.entity.Provider;
import com.yongy.dotori.domain.user.entity.Role;
import com.yongy.dotori.domain.user.entity.User;
import com.yongy.dotori.domain.user.repository.UserRepository;
import com.yongy.dotori.global.common.BaseResponseBody;
import com.yongy.dotori.global.redis.RedisUtil;
import jakarta.transaction.Transactional;
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

import java.time.LocalDate;

@Slf4j
@Transactional
@Service
public class NaverService {

    @Value("${naver.client.id}")
    private String NAVER_CLIENT_ID;

    @Value("${naver.client.secret}")
    private String NAVER_CLIENT_SECRET;

    @Value("${naver.redirect.url}")
    private String NAVER_REDIRECT_URL;

    @Autowired
    private RedisUtil redisUtil;
    private final static String NAVER_AUTH_URI = "https://nid.naver.com";
    private final static String NAVER_API_URI = "https://openapi.naver.com";

    @Autowired
    private UserRepository userRepository;
    private String accessToken;

    private String refreshToken;

    private final long exp = 1000L * 60 * 60;

    public String getNaverLogin() {
        return NAVER_AUTH_URI + "/oauth2.0/authorize"
                + "?client_id=" + NAVER_CLIENT_ID
                + "&redirect_uri=" + NAVER_REDIRECT_URL
                + "&response_type=code";
    }

    // TODO : 새로운 accessToken, refreshToken을 발급하기
    public ResponseEntity<? extends BaseResponseBody> newTokens(String code) throws Exception {
        if (code == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(404, "인증코드가 존재하지 않습니다."));

        try{
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-type", "application/x-www-form-urlencoded");

            // body에 담을 데이터를 저장함
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", NAVER_CLIENT_ID);
            params.add("client_secret", NAVER_CLIENT_SECRET); // 필수 X => 보안을 위해
            params.add("code", code);
            params.add("redirect_uri", NAVER_REDIRECT_URL);

            RestTemplate restTemplate = new RestTemplate();

            // request에 header, data를 저장한다.
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

            // HTTP 요청을 보내고 응답을 받음(통신)
            ResponseEntity<String> response = restTemplate.exchange(
                    NAVER_AUTH_URI +"/oauth2.0/token",
                    HttpMethod.POST,
                    httpEntity,
                    String.class
            );

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

            redisUtil.setDataExpire(user.getId(), refreshToken, exp * 24);
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, accessToken));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of(404, "인증코드가 유효하지 않습니다."));
        }
    }

    // TODO : accessToken으로 사용자의 정보 가져오기
    public ResponseEntity<? extends BaseResponseBody> getUserInfo(String accessToken) throws Exception{

        try{
            //HttpHeader 생성
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);
            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            //HttpHeader 담기
            RestTemplate rt = new RestTemplate();
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
            ResponseEntity<String> response = rt.exchange(
                    NAVER_API_URI + "/v1/nid/me",
                    HttpMethod.GET,
                    httpEntity,
                    String.class
            );

            //Response 데이터 파싱
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObj    = (JSONObject) jsonParser.parse(response.getBody());
            JSONObject account = (JSONObject) jsonObj.get("response");

            //사용자 정보 매핑
            String id = String.valueOf(account.get("email"));
            String name = String.valueOf(account.get("name"));

            String[] str = String.valueOf(account.get("mobile")).split("-");
            String phoneNumber = new StringBuilder().append(str[0]).append(str[1]).append(str[2]).toString();

            String birthDate = new StringBuilder().append(String.valueOf(account.get("birthyear"))).append("-")
                    .append(String.valueOf(account.get("birthday"))).toString();


            User user = User.builder()
                    .id(String.valueOf(id))
                    .userName(name)
                    .phoneNumber(phoneNumber)
                    .role(Role.USER)
                    .authProvider(Provider.NAVER)
                    .birthDate(LocalDate.parse(birthDate)).build();

            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, user));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of(404, "accessToken이 유효하지 않습니다."));
        }
    }
}
