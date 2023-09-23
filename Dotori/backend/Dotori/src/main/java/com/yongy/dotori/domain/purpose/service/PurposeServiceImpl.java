package com.yongy.dotori.domain.purpose.service;

import com.yongy.dotori.domain.purpose.dto.*;
import com.yongy.dotori.domain.purpose.entity.Purpose;
import com.yongy.dotori.domain.purpose.repository.PurposeRepository;
import com.yongy.dotori.domain.purposeData.repository.PurposeDataRepository;
import com.yongy.dotori.domain.user.entity.User;
import com.yongy.dotori.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PurposeServiceImpl implements PurposeService{

    private final PurposeRepository purposeRepository;
    private final PurposeDataRepository purposeDataRepository;

    @Override
    public void createPurpose(PurposeDTO purposeDTO) {
        User loginUser = this.getLoginUser();
        // 새로운 목표 DB에 저장
        purposeRepository.save(Purpose.builder()
                        .user(loginUser)
                        .purposeTitle(purposeDTO.getPurposeTitle())
                        .goalAmount(purposeDTO.getGoalAmount())
                        .currentBalance(BigDecimal.ZERO)
                        .startedAt(purposeDTO.getStartedAt())
                        .endAt(purposeDTO.getEndAt())
                        .terminated(false)
                        .terminatedAt(purposeDTO.getEndAt())
                .build());
    }

    @Override
    public PurposeAllDTO findAllPurpose() {
        User loginUser = this.getLoginUser();
        List<Purpose> purposeList = purposeRepository.findAllByUserUserSeqAndTerminatedIsFalse(loginUser.getUserSeq());

        List<PurposeListDTO> list = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        // 전체 목표에서 title, currentBalance, goalAmount 데이터만 뽑아오기
        for(Purpose p : purposeList){
            if(p.isTerminated()){
                continue;
            }

            list.add(PurposeListDTO.builder()
                            .purposeSeq(p.getPurposeSeq())
                            .title(p.getPurposeTitle())
                            .currentBalance(p.getCurrentBalance())
                            .goalAmount(p.getGoalAmount())
                    .build());
            total.add(p.getCurrentBalance()); // 현재 총 저축액 구하기 위해서 각 목표의 currentBalance 합 구하기
        }

        // PurposeALlDTO 형태로 반환
        PurposeAllDTO result = PurposeAllDTO.builder()
                .currentTotalSavings(total)
                .purposeList(list)
                .build();
        return result;
    }


    @Override
    public PurposeDetailDTO findPurposeDetail(Long purposeSeq) {
        // 목표 상세 내역 조회
        Purpose purpose = purposeRepository.findByPurposeSeq(purposeSeq);

        PurposeDetailDTO detail = PurposeDetailDTO.builder()
                .purposeTitle(purpose.getPurposeTitle())
                .currentBalance(purpose.getCurrentBalance())
                .goalAmount(purpose.getGoalAmount())
                .purposeDataList(purposeDataRepository.findAllByPurposeDataSeq(purposeSeq))
                .build();

        return detail;
    }


    @Override
    public void terminatePurpose(Long purposeSeq) {
        // 목표 중단
        Purpose purpose = purposeRepository.findByPurposeSeq(purposeSeq);

        purpose.update(Purpose.builder()
                .endAt(LocalDateTime.now())
                .terminatedAt(LocalDateTime.now())
                .terminated(true)
                .build());
    }

    @Override
    public PurposeSummaryDTO summarizePurpose(Long purposeSeq){
        Purpose purpose = purposeRepository.findByPurposeSeq(purposeSeq);
        BigDecimal goal = purpose.getGoalAmount();
        BigDecimal current = purpose.getCurrentBalance();
        BigDecimal percentage = BigDecimal.ZERO;

        // TODO 현재 진행된 퍼센트(or 남은 퍼센트) 정해야 됨
        if(goal != BigDecimal.ZERO || current != BigDecimal.ZERO){
            percentage = current.divide(goal, BigDecimal.ROUND_HALF_UP);
            percentage = percentage.multiply(new BigDecimal("100"));
        }

        PurposeSummaryDTO summary = PurposeSummaryDTO.builder()
                .purposeTitle(purpose.getPurposeTitle())
                .goalAmount(purpose.getGoalAmount())
                .currentBalance(purpose.getCurrentBalance())
                .restAmount(purpose.getGoalAmount().subtract(purpose.getCurrentBalance()))
                .percentage(percentage)
                .build();

        return summary;
    }

    public User getLoginUser(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
