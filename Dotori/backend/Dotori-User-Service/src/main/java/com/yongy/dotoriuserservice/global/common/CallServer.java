package com.yongy.dotoriuserservice.global.common;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

public class CallServer {
    // NOTE : POST호출 + Param 추가

    public ResponseEntity<String> postHttpBodyAndSend(String url, HashMap<String, Object> bodyData){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");

        HttpEntity<HashMap<String, Object>> httpEntity = new HttpEntity<>(bodyData, headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        return response;
    }
}
