package com.yongy.dotoripurposeservice.global.common;

import com.yongy.dotoripurposeservice.domain.purpose.dto.communication.BankDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class CallServer {


    // NOTE : requestBody로 데이터 보낼 때
    public ResponseEntity<String> getHttpBodyAndSend(String url, HttpMethod method, HashMap<String, Object> bodyData){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");

        HttpEntity<HashMap<String, Object>> httpEntity = new HttpEntity<>(bodyData, headers);

        RestTemplate restTemplate = new RestTemplate();
        log.info("------START------");
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                method,
                httpEntity,
                String.class
        );
        log.info("------END------");
        return response;
    }

    // NOTE : parameter로 데이터 보낼 때
    public ResponseEntity<String> getHttpWithParamsAndSend(String url, MultiValueMap<String, Long> params){
        // HTTP 헤더 설정
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url+"/{bankSeq}", String.class, params);

        return response;
    }

    public ResponseEntity<String> postHttpWithParamsAndSend(String url, MultiValueMap<String, String> parameters){

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, parameters, String.class);

        return response;
    }


}
