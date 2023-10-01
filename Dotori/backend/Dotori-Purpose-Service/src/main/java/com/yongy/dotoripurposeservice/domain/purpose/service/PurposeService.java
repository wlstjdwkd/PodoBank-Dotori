package com.yongy.dotoripurposeservice.domain.purpose.service;


import com.yongy.dotoripurposeservice.domain.purpose.dto.PurposeAllDTO;
import com.yongy.dotoripurposeservice.domain.purpose.dto.PurposeDTO;
import com.yongy.dotoripurposeservice.domain.purpose.dto.PurposeDetailDTO;
import com.yongy.dotoripurposeservice.domain.purpose.dto.PurposeSummaryDTO;
import com.yongy.dotoripurposeservice.domain.purpose.dto.communication.SavingDTO;
import com.yongy.dotoripurposeservice.domain.purpose.dto.communication.SavingDataDTO;

public interface PurposeService {
    void createPurpose(PurposeDTO purposeDTO);
    PurposeAllDTO findAllPurpose(Long userSeq);
    PurposeDetailDTO findPurposeDetail(Long purposeSeq);
    void terminatePurpose(Long purposeSeq);
    PurposeSummaryDTO summarizePurpose(Long purposeSeq);

    void saving(SavingDataDTO savingDataDTO);



}
