package com.yongy.dotorimainservice.domain.chatGPT.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yongy.dotorimainservice.domain.chatGPT.dto.*;
import com.yongy.dotorimainservice.domain.payment.entity.Payment;
import com.yongy.dotorimainservice.domain.payment.repository.PaymentRepository;
import com.yongy.dotorimainservice.domain.plan.dto.ActiveCategoryDTO;
import com.yongy.dotorimainservice.domain.planDetail.repository.PlanDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatGPTService {
    private final PaymentRepository paymentRepository;

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

        // 들어온 객체에 따라 message 및 request 만들기
        RequestDTO requestDTO = createRequest(categoryDataDTO);

        // API 요청 본문 설정
        String requestBody = objectMapper.writeValueAsString(requestDTO);
        request.setEntity(new StringEntity(requestBody, "UTF-8"));

        // API 호출 및 응답 처리
        HttpResponse response = httpClient.execute(request);
        String responseContent = EntityUtils.toString(response.getEntity(),"UTF-8");

        ResponseDetailDTO responseDetailDTO = objectMapper.readValue(responseContent, ResponseDetailDTO.class);
        Message message = responseDetailDTO.getChoices().get(0).getMessage();

        objectMapper = new ObjectMapper();
        Map<String, List<ActiveCategoryDTO>> dataMap = objectMapper.readValue(message.getContent(), Map.class);

        List<ResultDataDTO> result = new ArrayList<>();
        for(Map.Entry<String, List<ActiveCategoryDTO>> entry : dataMap.entrySet()){
            result.add(ResultDataDTO.builder()
                    .categoryGroupName(entry.getKey())
                    .categories(entry.getValue())
                    .build());
        }

        return result;
    }

    // NOTE : ChatGPT에 요청할 Request 만드는 메소드
    public RequestDTO createRequest(Object object){
        List<Message> messageList = new ArrayList<>();

        if(object instanceof CategoryDataDTO){
            messageList.add(Message.builder().role("system").content("JSON result like {categoryGroup1:[{categoryName1, targetAmount1}, {categoryName2,targetAmount2}...], categoryGroup2:[{category3, targetAmount3} , {category4, targetAmount4}, ...], ...}").build());
            messageList.add(Message.builder().role("system").content("all categories must belong to one categoryGroup").build());
            messageList.add(Message.builder().role("user").content(object.toString()).build());
        }

        if(object instanceof UnclassifiedDataDTO){
            messageList.add(Message.builder().role("system").content("All element of payments must belong to one planDetails.").build());
            messageList.add(Message.builder().role("system").content("import java.util.List;\n" +
                    "\n" +
                    "@Builder\n" +
                    "@Getter\n" +
                    "@NoArgsConstructor\n" +
                    "@AllArgsConstructor\n" +
                    "public class UnclassifiedResponseDTO {\n" +
                    "    Long planDetailSeq;\n" +
                    "    List<Long> paymentSeqs;\n" +
                    "}").build());
            messageList.add(Message.builder().role("system").content("JSON result what List<UnclassifiedResponseDTO> type.").build());
            messageList.add(Message.builder().role("user").content(object.toString()).build());
            log.info(object.toString());
        }

        RequestDTO requestDTO = RequestDTO.builder()
                .model("gpt-3.5-turbo")
                .messages(messageList)
                .temperature(1.0)
                .maxTokens(256)
                .topP(1)
                .frequencyPenalty(0)
                .presencePenalty(0)
                .build();

        return requestDTO;
    }

    public void getPaymentChatGPTResponse(UnclassifiedDataDTO unclassifiedDataDTO) throws IOException {
        // 계좌에 연결된 planDetail에 연결된 category 리스트랑 payment 리스트에 있는 name이랑 분류
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost request = new HttpPost(API_URL);

        // API 요청 헤더 설정
        request.setHeader("Authorization", "Bearer " + API_KEY);
        request.setHeader("Content-Type", "application/json");

        // 대화 메시지 구성
        ObjectMapper objectMapper = new ObjectMapper();

        // 들어온 객체에 따라 message 및 request 만들기
        RequestDTO requestDTO = createRequest(unclassifiedDataDTO);

        // API 요청 본문 설정
        String requestBody = objectMapper.writeValueAsString(requestDTO);
        request.setEntity(new StringEntity(requestBody, "UTF-8"));

        // API 호출 및 응답 처리
        HttpResponse response = httpClient.execute(request);
        String responseContent = EntityUtils.toString(response.getEntity(),"UTF-8");

        ResponseDetailDTO responseDetailDTO = objectMapper.readValue(responseContent, ResponseDetailDTO.class);
        Message message = responseDetailDTO.getChoices().get(0).getMessage();
        log.info("메세지\n"+message.getContent());

        objectMapper = new ObjectMapper();
        List<UnclassifiedResponseDTO> data = objectMapper.readValue(message.getContent(), new TypeReference<List<UnclassifiedResponseDTO>>() {});
        List<Payment> result = new ArrayList<>();

        log.info(data.size()+"");

        int count = 0;

        for(UnclassifiedResponseDTO temp : data){
            for(Long paymentSeq : temp.getPaymentSeqs()){
                Payment tempPayment = paymentRepository.findByPaymentSeq(paymentSeq);
                tempPayment.updatePlanDetail(temp.getPlanDetailSeq());
                result.add(tempPayment);
            }
            count += temp.getPaymentSeqs().size();
        }

        log.info("결제 내역 개수 : "+count);
        paymentRepository.saveAll(result);

    }
}
