package com.bank.podo.global.request;

import com.bank.podo.domain.account.dto.CheckSuccessCodeDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RequestUtil {

    @Value("${http.request.auth.url}")
    private String authUrl;

    private final RequestApi requestApi;

    public boolean checkVerificationSuccess(CheckSuccessCodeDTO checkSuccessCodeDTO) {
        String url = authUrl + "/api/v1/auth/checkSuccessCode";

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(checkSuccessCodeDTO);
            return requestApi.apiPost(url, json);
        } catch (Exception e) {
            return false;
        }
    }
}
