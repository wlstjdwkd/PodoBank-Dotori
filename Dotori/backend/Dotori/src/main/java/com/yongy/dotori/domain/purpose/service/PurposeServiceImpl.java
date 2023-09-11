package com.yongy.dotori.domain.purpose.service;

import com.yongy.dotori.domain.purpose.dto.PurposeAllDTO;
import com.yongy.dotori.domain.purpose.dto.PurposeDTO;
import com.yongy.dotori.domain.purpose.dto.PurposeDetailDTO;
import com.yongy.dotori.domain.purpose.dto.PurposeSummaryDTO;
import com.yongy.dotori.domain.purpose.entity.Purpose;
import com.yongy.dotori.domain.purpose.repository.PurposeRepository;
import com.yongy.dotori.domain.purposeData.repository.PurposeDataRepository;
import com.yongy.dotori.domain.user.entity.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PurposeServiceImpl implements PurposeService{

    private PurposeRepository purposeRepository;
    private PurposeDataRepository purposeDataRepository;
    @Override
    public void createPurpose(PurposeDTO purposeDTO) {
        // 새로운 목표 DB에 저장
        purposeRepository.save(Purpose.builder()
                        .user(null)
                        .purposeTitle(purposeDTO.getPurposeTitle())
                        .goalAmount(purposeDTO.getGoalAmount())
                        .currentBalance(BigDecimal.ZERO)
                        .startedAt(purposeDTO.getStartedAt())
                        .endAt(purposeDTO.getEndAt())
                        .isTerminated(false)
                        .terminatedAt(purposeDTO.getEndAt())
                .build());
    }

    @Override
    public PurposeAllDTO findAllPurpose() {
        List<Purpose> purposeList = purposeRepository.findAllByUserUserSeq((long)1);
        List<PurposeSummaryDTO> list = new ArrayList<>();

        for(Purpose p : purposeList){
            list.add(PurposeSummaryDTO.builder()
                            .title(p.getPurposeTitle())
                            .currentBalance(p.getCurrentBalance())
                            .goalAmount(p.getGoalAmount())
                    .build());
        }

        PurposeAllDTO result = PurposeAllDTO.builder()
                // 현재 저축액 구해야 함
                .purposeList(list)
                .build();
        return result;
    }


    @Override
    public PurposeDetailDTO findPurposeDetail(Long purposeSeq) {
        // 목표 상세 내역 조회
        Purpose purpose = purposeRepository.findByPurposeSeq();

        PurposeDetailDTO detail = PurposeDetailDTO.builder()
                .purposeTitle(purpose.getPurposeTitle())
                .currentBalance(purpose.getCurrentBalance())
                .goalAmount(purpose.getGoalAmount())
                .purposeDataList(purposeDataRepository.findAllByPurposeDataSeq(purposeSeq))
                .build();

        return detail;
    }
}
