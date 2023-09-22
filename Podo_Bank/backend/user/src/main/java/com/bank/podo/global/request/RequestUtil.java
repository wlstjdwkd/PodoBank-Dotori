package com.bank.podo.global.request;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RequestUtil {

    @Value("${http.request.account.url}")
    private String accountUrl;

    @Value("${http.request.auth.url}")
    private String authUrl;

    private final RequestApi requestApi;

    public boolean checkAccountBalanceZero(Long userId) {
        String url = accountUrl + "/api/v1/account/" + userId;

        return requestApi.apiGet(url);
    }

    public boolean removeRefreshToken(String email) {
        String url = authUrl + "/api/v1/auth/logout/" + email;

        return requestApi.apiPost(url, "");
    }

    public boolean checkVerificationSuccess(String successCode, String type) {
        String url = authUrl + "/api/v1/auth/verification/" + type + "/" + successCode;

        return requestApi.apiGet(url);
    }
}
