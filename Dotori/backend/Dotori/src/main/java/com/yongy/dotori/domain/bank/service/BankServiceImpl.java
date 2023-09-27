package com.yongy.dotori.domain.bank.service;

import com.yongy.dotori.domain.bank.dto.response.BankListDto;
import com.yongy.dotori.domain.bank.entity.Bank;
import com.yongy.dotori.domain.bank.repository.BankRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class BankServiceImpl implements BankService{
    @Autowired
    private BankRepository bankRepository;

    public List<BankListDto> BankList(){
        List<Bank> list = bankRepository.findAll();

        List<BankListDto> bankListDto = new ArrayList<>();

        for(Bank bank : list){
            bankListDto.add(BankListDto.builder().bankSeq(bank.getBankSeq()).bankName(bank.getBankName()).build());
        }
        return bankListDto;
    }
}
