package com.yongy.dotori.domain.account.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yongy.dotori.domain.account.dto.AccountDTO;

import java.util.List;

public interface AccountService {
    List<AccountDTO> findAllAccount() throws JsonProcessingException;
}
