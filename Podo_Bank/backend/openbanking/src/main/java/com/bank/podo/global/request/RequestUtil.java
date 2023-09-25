package com.bank.podo.global.request;

import com.bank.podo.domain.user.entity.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestUtil {

    @Value("${http.request.auth.url}")
    private String authUrl;

    @Value("${http.request.user.url}")
    private String userUrl;

    @Value("${http.request.account.url}")
    private String accountUrl;

    private final RequestApi requestApi;

    public User getUser(String email) {
        String url = userUrl + "/api/v1/user/userInfo";

        try {
            ObjectMapper objectMapper = new ObjectMapper()
                    .registerModule(new JavaTimeModule());
            JsonNode jsonNode = requestApi.apiPostGetBody(url, email);
            if(jsonNode != null) {
                User user = objectMapper.readValue(jsonNode.toString(), User.class);
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
