package com.yongy.dotorimainservice.domain.account.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yongy.dotorimainservice.domain.account.dto.AccountDto;
import com.yongy.dotorimainservice.domain.account.dto.AccountListDto;
import com.yongy.dotorimainservice.domain.account.dto.communication.AccountNumberTitleReqDto;
import com.yongy.dotorimainservice.domain.account.dto.communication.AccountReqDto;
import com.yongy.dotorimainservice.domain.account.entity.Account;
import org.json.simple.parser.ParseException;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    List<AccountListDto> findAllAccount() throws JsonProcessingException, ParseException;
    void removeUserAllAccounts(Long userSeq) throws ParseException;
    Account getUserAccount(String accountNumber);

    void saveAccountTitle(AccountNumberTitleReqDto accountNumberTitleReqDto);

    void saveAccount(AccountReqDto accountReqDto);
    BigDecimal getBalance(Long accountSeq) throws JsonProcessingException, ParseException;

    void removeUserAccount(Long accountSeq) throws ParseException;

    AccountDto findAccount(Long planSeq) throws ParseException, JsonProcessingException;



}
