package com.yongy.dotori.domain.purpose.service;

import com.yongy.dotori.domain.purpose.dto.PurposeAllDTO;
import com.yongy.dotori.domain.purpose.dto.PurposeDTO;
import com.yongy.dotori.domain.purpose.dto.PurposeDetailDTO;
import com.yongy.dotori.domain.purpose.entity.Purpose;

public interface PurposeService {
    void createPurpose(PurposeDTO purposeDTO);

    PurposeAllDTO findAllPurpose();
    PurposeDetailDTO findPurposeDetail(Long purposeSeq);

}
