package com.yongy.dotorimainservice.global.communication;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Configurable
@RequiredArgsConstructor
public class ApiForm {

    @Value("${dotori.user.url}")
    private String DOTORI_USER_URL;

    @Value("${dotori.purpose.url}")
    private String DOTORI_PURPOSE_URL;

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<String> sendPostData(String endPoint, HashMap<String, String> bodyData,
                                                HttpMethod httpMethod){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");

        HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(bodyData, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                DOTORI_USER_URL+endPoint,
                        httpMethod,
                        httpEntity,
                        String.class
        );

        return response;
    }

}
