package com.yongy.dotori.domain.chatGPT.controller;

import com.yongy.dotori.domain.categoryGroup.entity.CategoryGroup;
import com.yongy.dotori.domain.chatGPT.dto.*;
import com.yongy.dotori.domain.chatGPT.service.ChatGPTService;
import com.yongy.dotori.domain.payment.entity.Payment;
import com.yongy.dotori.global.scheduler.PaymentsScheduler;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/chatgpt")
public class ChatGPTController {

    private final ChatGPTService chatGPTService;
    private final PaymentsScheduler paymentsScheduler;

    @Operation(summary = "카테고리그룹에 따라 카테고리 분류")
    @PostMapping()
    public ResponseEntity<List<ResultDataDTO>> categoryChatGPT(@RequestBody CategoryDataDTO categoryDataDTO) throws Exception {
        return ResponseEntity.ok().body(chatGPTService.getChatGPTResponse(categoryDataDTO));
    }

    @Operation(summary = "카테고리에 따라 payment 분류")
    @GetMapping("/unclassified")
    public ResponseEntity<Void> unclassifiedChatGPT() throws IOException, ParseException {
        paymentsScheduler.getPayments();
        return ResponseEntity.ok().build();
    }
}
