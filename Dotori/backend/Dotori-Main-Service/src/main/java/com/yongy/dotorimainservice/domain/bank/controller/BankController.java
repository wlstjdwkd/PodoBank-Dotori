package com.yongy.dotorimainservice.domain.bank.controller;


import com.yongy.dotorimainservice.domain.bank.dto.response.BankListDto;
import com.yongy.dotorimainservice.domain.bank.repository.BankRepository;
import com.yongy.dotorimainservice.domain.bank.service.BankServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/bank")
public class BankController {

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private BankServiceImpl bankServiceImpl;

    @GetMapping
    public ResponseEntity<List<BankListDto>> bankList(){
        return ResponseEntity.ok().body(bankServiceImpl.BankList());
    }
}
