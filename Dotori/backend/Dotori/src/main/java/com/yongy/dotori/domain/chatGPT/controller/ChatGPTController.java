package com.yongy.dotori.domain.chatGPT.controller;

import com.yongy.dotori.domain.categoryGroup.entity.CategoryGroup;
import com.yongy.dotori.domain.chatGPT.dto.CategoryDataDTO;
import com.yongy.dotori.domain.chatGPT.dto.RequestDTO;
import com.yongy.dotori.domain.chatGPT.dto.ResultDataDTO;
import com.yongy.dotori.domain.chatGPT.service.ChatGPTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/chat-gpt")
public class ChatGPTController {

    private final ChatGPTService chatGPTService;

    @PostMapping()
    public List<ResultDataDTO> chat(@RequestBody CategoryDataDTO categoryDataDTO) throws Exception {
        return chatGPTService.getChatGPTResponse(categoryDataDTO);
    }
}
