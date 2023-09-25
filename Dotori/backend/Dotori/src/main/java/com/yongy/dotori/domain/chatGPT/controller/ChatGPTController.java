package com.yongy.dotori.domain.chatGPT.controller;

import com.yongy.dotori.domain.categoryGroup.entity.CategoryGroup;
import com.yongy.dotori.domain.chatGPT.dto.*;
import com.yongy.dotori.domain.chatGPT.service.ChatGPTService;
import com.yongy.dotori.domain.payment.entity.Payment;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Operation(summary = "카테고리그룹에 따라 카테고리 분류")
    @PostMapping()
    public ResponseEntity<List<ResultDataDTO>> categoryChatGPT(@RequestBody CategoryDataDTO categoryDataDTO) throws Exception {
        return ResponseEntity.ok().body(chatGPTService.getChatGPTResponse(categoryDataDTO));
    }

    @Operation(summary = "카테고리에 따라 payment 분류")
    @GetMapping("/unclassified")
    public ResponseEntity<List<UnclassifiedResponseDTO>> unclassifiedChatGPT(UnclassifiedDataDTO unclassifiedDataDTO) throws IOException {
        return ResponseEntity.ok().body(chatGPTService.getPaymentChatGPTResponse(unclassifiedDataDTO));
    }
}
