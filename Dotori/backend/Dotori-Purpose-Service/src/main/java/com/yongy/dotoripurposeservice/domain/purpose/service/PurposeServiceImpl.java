package com.yongy.dotoripurposeservice.domain.purpose.service;


import com.yongy.dotoripurposeservice.domain.account.entity.Account;
import com.yongy.dotoripurposeservice.domain.account.repository.AccountRepository;
import com.yongy.dotoripurposeservice.domain.purpose.dto.*;
import com.yongy.dotoripurposeservice.domain.purpose.entity.Purpose;
import com.yongy.dotoripurposeservice.domain.purpose.repository.PurposeRepository;
import com.yongy.dotoripurposeservice.domain.purposeData.dto.PurposeDataDTO;
import com.yongy.dotoripurposeservice.domain.purposeData.entity.PurposeData;
import com.yongy.dotoripurposeservice.domain.purposeData.repository.PurposeDataRepository;
import com.yongy.dotoripurposeservice.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PurposeServiceImpl implements PurposeService{

    private final PurposeRepository purposeRepository;
    private final PurposeDataRepository purposeDataRepository;
    private final AccountRepository accountRepository;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void createPurpose(PurposeDTO purposeDTO) {
        User loginUser = this.getLoginUser();
        // 새로운 목표 DB에 저장
        purposeRepository.save(Purpose.builder()
                        .userSeq(loginUser.getUserSeq()) // TODO : 통신
                        .purposeTitle(purposeDTO.getPurposeTitle())
                        .goalAmount(purposeDTO.getGoalAmount())
                        .currentBalance(BigDecimal.ZERO)
                        .startedAt(purposeDTO.getStartedAt())
                        .endAt(purposeDTO.getEndAt())
                        .terminatedAt(null)
                .build());
    }

    @Override
    public PurposeAllDTO findAllPurpose() {
        User loginUser = this.getLoginUser();
        List<Purpose> purposeList = purposeRepository.findAllByUserSeqAndTerminatedAtIsNull(loginUser.getUserSeq());

        List<PurposeListDTO> list = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        // 전체 목표에서 title, currentBalance, goalAmount 데이터만 뽑아오기
        for(Purpose p : purposeList){
            if(p.getTerminatedAt() != null){
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
        List<PurposeData> list = purposeDataRepository.findAllByPurpose(purpose);
        log.info(list.size()+"");
        List<PurposeDataDTO> purposeData = new ArrayList<>();

        for(PurposeData data : list){
            Account account = accountRepository.findByAccountSeqAndDeleteAtIsNull(data.getAccountSeq());
            purposeData.add(PurposeDataDTO.builder()
                            .dataName(account.getAccountTitle())
                            .dataAmount(data.getDataAmount())
                            .dataCurrentBalance(data.getDataCurrentBalance())
                            .dataCreatedAt(data.getDataCreatedAt().format(formatter))
                    .build());
        }

        PurposeDetailDTO detail = PurposeDetailDTO.builder()
                .purposeTitle(purpose.getPurposeTitle())
                .currentBalance(purpose.getCurrentBalance())
                .goalAmount(purpose.getGoalAmount())
                .startedAt(purpose.getStartedAt().toString())
                .endAt(purpose.getEndAt().toString())
                .purposeDataList(purposeData)
                .build();

        return detail;
    }


    @Override
    public void terminatePurpose(Long purposeSeq) {
        // 목표 중단
        Purpose purpose = purposeRepository.findByPurposeSeq(purposeSeq);

        purpose.update(Purpose.builder()
                .endAt(LocalDate.now())
                .terminatedAt(LocalDateTime.now())
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
