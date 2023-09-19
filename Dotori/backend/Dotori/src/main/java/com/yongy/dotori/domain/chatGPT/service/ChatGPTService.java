package com.yongy.dotori.domain.chatGPT.service;
import com.yongy.dotori.domain.categoryGroup.entity.CategoryGroup;
import com.yongy.dotori.domain.chatGPT.dto.CategoryDataDTO;
import com.yongy.dotori.domain.chatGPT.dto.Message;
import com.yongy.dotori.domain.chatGPT.dto.RequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ChatGPTService {
    @Value("${chatGPT.api-key}")
    private String API_KEY;
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private RestTemplate restTemplate;
    public String getChatGPTResponse(CategoryDataDTO categoryDataDTO) throws Exception {

        HttpClient httpClient = HttpClients.createDefault();
        HttpPost request = new HttpPost(API_URL);

        // API 요청 헤더 설정
        request.setHeader("Authorization", "Bearer " + API_KEY);
        request.setHeader("Content-Type", "application/json");

        // 대화 메시지 구성
        ObjectMapper objectMapper = new ObjectMapper();

        // 카테고리 그룹, 카테고리를 String으로 바꿔서 message로 전달
        List<Message> messageList = new ArrayList<>();
        messageList.add(Message.builder().role("user").content(categoryDataDTO.toString()).build());
        messageList.add(Message.builder().role("assistant").content("json형식으로 리턴해줘.카테고리그룹을 key값으로 하고 value는 List형식으로 해당되는 카테고리 반환.").build());
        messageList.add(Message.builder().role("assistant").content("각 카테고리는 하나의 카테고리그룹에 속해야함. 각 카테고리는 중복 안됨").build());

        RequestDTO requestDTO = RequestDTO.builder()
                .model("gpt-3.5-turbo")
                .messages(messageList)
                .temperature(1.0)
                .maxTokens(256)
                .topP(1)
                .frequencyPenalty(0)
                .presencePenalty(0)
                .build();

        // API 요청 본문 설정
        String requestBody = objectMapper.writeValueAsString(requestDTO);
        log.info(requestBody);
        request.setEntity(new StringEntity(requestBody, "UTF-8"));

        // API 호출 및 응답 처리
        HttpResponse response = httpClient.execute(request);
        String responseContent = EntityUtils.toString(response.getEntity(),"UTF-8");

        log.info(responseContent);
        return responseContent;
    }
}
