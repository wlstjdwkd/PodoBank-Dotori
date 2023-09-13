package com.bank.podo.domain.openbank.controller;

import com.bank.podo.domain.openbank.dto.FintechOneCentVerificationCheckDTO;
import com.bank.podo.domain.openbank.dto.FintechOneCentVerificationDTO;
import com.bank.podo.domain.openbank.service.FintechService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fintech")
public class FintechController {

    private final FintechService fintechService;

    @PostMapping("/oneCentVerification")
    public void oneCentVerification(@RequestBody FintechOneCentVerificationDTO fintechOneCentVerificationDTO) {
        fintechService.oneCentVerification(fintechOneCentVerificationDTO);
    }

    @PostMapping("/oneCentVerification/check")
    public void oneCentVerificationCheck(@RequestBody FintechOneCentVerificationCheckDTO fintechOneCentVerificationDTO) {
        fintechService.oneCentVerificationCheck(fintechOneCentVerificationDTO);
    }
}
