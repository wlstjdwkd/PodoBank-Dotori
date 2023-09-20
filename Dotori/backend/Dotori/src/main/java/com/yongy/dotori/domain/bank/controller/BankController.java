package com.yongy.dotori.domain.bank.controller;

import com.yongy.dotori.domain.bank.dto.response.BankListDto;
import com.yongy.dotori.domain.bank.entity.Bank;
import com.yongy.dotori.domain.bank.repository.BankRepository;
import com.yongy.dotori.domain.bank.service.BankService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/bank")
public class BankController {

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private BankService bankService;

    @GetMapping
    public List<BankListDto> bankList(){
        return bankService.BankList();
    }
}
