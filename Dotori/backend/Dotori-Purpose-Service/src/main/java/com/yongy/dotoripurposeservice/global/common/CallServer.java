package com.yongy.dotoripurposeservice.global.common;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class CallServer {

    // NOTE : requestBody로 데이터 보낼 때
    public ResponseEntity<String> getHttpBodyAndSend(String url, HashMap<String, Object> bodyData){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");

        HttpEntity<HashMap<String, Object>> httpEntity = new HttpEntity<>(bodyData, headers);

        RestTemplate restTemplate = new RestTemplate();
        log.info("------START------");
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                httpEntity,
                String.class
        );
        log.info("------END------");
        return response;
    }

    // NOTE : parameter로 데이터 보낼 때
    public ResponseEntity<String> getHttpWithParamsAndSend(String url, HashMap<String, Object> params){
        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");

        // URL 매개변수를 이용한 요청 URL 생성
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        for (Map.Entry<String, Object> entity : params.entrySet()) {
            builder.queryParam(entity.getKey(), entity.getValue());
        }

        String finalUrl = builder.build().toUriString();
        log.info("------START------");

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                finalUrl,
                HttpMethod.GET,
                null,
                String.class
        );
        log.info("------END------");
        return response;
    }


}
