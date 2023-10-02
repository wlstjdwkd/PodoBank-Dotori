package com.yongy.dotorimainservice.domain.planDetail.service;

import com.yongy.dotorimainservice.domain.category.entity.Category;
import com.yongy.dotorimainservice.domain.category.repository.CategoryRepository;
import com.yongy.dotorimainservice.domain.plan.repository.PlanRepository;
import com.yongy.dotorimainservice.domain.plan.service.PlanService;
import com.yongy.dotorimainservice.domain.planDetail.dto.response.PlanDetailListResDto;
import com.yongy.dotorimainservice.domain.planDetail.dto.response.SpecificationDTO;
import com.yongy.dotorimainservice.domain.planDetail.entity.PlanDetail;
import com.yongy.dotorimainservice.domain.planDetail.repository.PlanDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlanDetailServiceImpl implements PlanDetailService{
    private final PlanRepository planRepository;
    private final PlanDetailRepository planDetailRepository;
    private final CategoryRepository categoryRepository;

    public SpecificationDTO getPlanDetail(Long planSeq) {
        List<PlanDetail> planDetailList = planDetailRepository.findAllByPlanPlanSeq(planSeq);

        List<PlanDetailListResDto> resultPlanDetailList = new ArrayList<>();

        for (PlanDetail planDetail : planDetailList) {
            Category category = categoryRepository.findByCategorySeq(planDetail.getCategory().getCategorySeq());
            resultPlanDetailList.add(PlanDetailListResDto.builder()
                    .categoryTitle(category.getCategoryTitle()) // 카테고리 이름
                    .expense(planDetail.getDetailLimit().subtract(planDetail.getDetailBalance())) // 소비
                    .savings(planDetail.getDetailBalance()).build()); // 저축
        }

        return SpecificationDTO.builder()
                .planDetailList(resultPlanDetailList)
                .additionalSaving(planRepository.findByPlanSeq(planSeq).getAdditionalSaving()) // 저축전이면 null
                .build();
    }
}
