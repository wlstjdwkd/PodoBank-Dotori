package com.yongy.dotori.domain.chatGPT.service;
import com.yongy.dotori.domain.chatGPT.dto.*;
import com.yongy.dotori.domain.plan.dto.CategoryDTO;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ChatGPTService {
    @Value("${chatGPT.api-key}")
    private String API_KEY;
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    public List<ResultDataDTO> getChatGPTResponse(CategoryDataDTO categoryDataDTO) throws Exception {

        HttpClient httpClient = HttpClients.createDefault();
        HttpPost request = new HttpPost(API_URL);

        // API 요청 헤더 설정
        request.setHeader("Authorization", "Bearer " + API_KEY);
        request.setHeader("Content-Type", "application/json");

        // 대화 메시지 구성
        ObjectMapper objectMapper = new ObjectMapper();

        // 카테고리 그룹, 카테고리를 String으로 바꿔서 message로 전달
        List<Message> messageList = new ArrayList<>();
        messageList.add(Message.builder().role("system").content("JSON result like {categoryGroup1:[{categoryName1, targetAmount1}, {categoryName2,targetAmount2}...], categoryGroup2:[{category3, targetAmount3} , {category4, targetAmount4}, ...], ...}").build());
        messageList.add(Message.builder().role("system").content("all categories must belong to one categoryGroup").build());
        messageList.add(Message.builder().role("user").content(categoryDataDTO.toString()).build());
        log.info(categoryDataDTO.toString());

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
        request.setEntity(new StringEntity(requestBody, "UTF-8"));

        // API 호출 및 응답 처리
        HttpResponse response = httpClient.execute(request);
        String responseContent = EntityUtils.toString(response.getEntity(),"UTF-8");

        ResponseDetailDTO responseDetailDTO = objectMapper.readValue(responseContent, ResponseDetailDTO.class);
        Message message = responseDetailDTO.getChoices().get(0).getMessage();

        objectMapper = new ObjectMapper();
        Map<String, List<CategoryDTO>> dataMap = objectMapper.readValue(message.getContent(), Map.class);

        List<ResultDataDTO> result = new ArrayList<>();
        for(Map.Entry<String, List<CategoryDTO>> entry : dataMap.entrySet()){
            result.add(ResultDataDTO.builder()
                    .categoryGroupName(entry.getKey())
                    .categories(entry.getValue())
                    .build());
        }

        return result;
    }
}
