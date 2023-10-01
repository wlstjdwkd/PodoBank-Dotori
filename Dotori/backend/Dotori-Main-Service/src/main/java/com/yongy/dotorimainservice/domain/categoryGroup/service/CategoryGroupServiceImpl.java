package com.yongy.dotorimainservice.domain.categoryGroup.service;


import com.yongy.dotorimainservice.domain.categoryGroup.dto.CategoryGroupDTO;
import com.yongy.dotorimainservice.domain.categoryGroup.entity.CategoryGroup;
import com.yongy.dotorimainservice.domain.categoryGroup.repository.CategoryGroupRepository;
import com.yongy.dotorimainservice.domain.user.entity.User;
import com.yongy.dotorimainservice.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryGroupServiceImpl implements CategoryGroupService {

    private final CategoryGroupRepository categoryGroupRepository;
    private final UserRepository userRepository;
    @Override
    public List<CategoryGroupDTO> findAllCategoryGroup() {
        // 사용자에게 등록되어 있는 카데고리 그룹 리스트(Seq, 이름) 가져오기
        User loginUser = userRepository.findByIdAndExpiredAtIsNull("1");
        List<CategoryGroup> list = categoryGroupRepository.findAllByUserUserSeq(loginUser.getUserSeq());
        List<CategoryGroupDTO> result = new ArrayList<>();

        for(CategoryGroup group : list){
            result.add(CategoryGroupDTO.builder()
                            .categoryGroupSeq(group.getCategoryGroupSeq())
                            .groupTitle(group.getGroupTitle())
                    .build());
        }

        return result;
    }
}
