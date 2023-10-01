package com.yongy.dotorimainservice.global.common;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Slf4j
@Service
@AllArgsConstructor
public class CallServer {
    // NOTE : POST호출 + Param 추가

    public ResponseEntity<String> postHttpBodyAndSend(String url, HashMap<String, Object> bodyData){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");

        HttpEntity<HashMap<String, Object>> httpEntity = new HttpEntity<>(bodyData, headers);

        RestTemplate restTemplate = new RestTemplate();
        log.info("--test1--");
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                httpEntity,
                String.class
        );
        log.info("--test2--");
        return response;
    }

    public ResponseEntity<String> getHttpBodyAndSend(String url, HashMap<String, Object> bodyData){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");

        HttpEntity<HashMap<String, Object>> httpEntity = new HttpEntity<>(bodyData, headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                httpEntity,
                String.class
        );

        log.info("--test2--");
        return response;
    }


    public ResponseEntity<Void> patchHttpBodyAndSend(String url, Object bodyData){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");

        HttpEntity<Object> httpEntity = new HttpEntity<>(bodyData, headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.PATCH,
                httpEntity,
                String.class
        );

        return ResponseEntity.ok().build();
    }
}
