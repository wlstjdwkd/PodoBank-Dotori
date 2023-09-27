package com.yongy.dotoripurposeservice.domain.purpose.service;


import com.yongy.dotoripurposeservice.domain.purpose.dto.PurposeAllDTO;
import com.yongy.dotoripurposeservice.domain.purpose.dto.PurposeDTO;
import com.yongy.dotoripurposeservice.domain.purpose.dto.PurposeDetailDTO;
import com.yongy.dotoripurposeservice.domain.purpose.dto.PurposeSummaryDTO;

public interface PurposeService {
    void createPurpose(PurposeDTO purposeDTO);
    PurposeAllDTO findAllPurpose();
    PurposeDetailDTO findPurposeDetail(Long purposeSeq);
    void terminatePurpose(Long purposeSeq);
    PurposeSummaryDTO summarizePurpose(Long purposeSeq);

}
