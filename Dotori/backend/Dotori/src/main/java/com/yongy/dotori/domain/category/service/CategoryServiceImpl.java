package com.yongy.dotori.domain.category.service;

import com.yongy.dotori.domain.plan.dto.CategoryDTO;
import com.yongy.dotori.domain.category.dto.CategoryDetailDTO;
import com.yongy.dotori.domain.category.entity.Category;
import com.yongy.dotori.domain.category.repository.CategoryRepository;
//import org.springframework.security.core.context.SecurityContextHolder;
import com.yongy.dotori.domain.user.entity.User;
import com.yongy.dotori.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    public List<CategoryDetailDTO> findAllCategory(){
        // 카테고리 리스트 반환
        // User user = getLoginUser();
        User loginUser = userRepository.findByIdAndExpiredAtIsNull("1");
        // TODO 테스트 후 로그인된 유저정보로 조회하도록 고칠것~
        List<Category> categories = categoryRepository.findAllByUserUserSeq(loginUser.getUserSeq());
        List<CategoryDetailDTO> result = new ArrayList<>();
        for(Category c : categories){
            result.add(CategoryDetailDTO.builder()
                            .categorySeq(c.getCategorySeq())
                            .categoryTitle(c.getCategoryTitle())
                    .build());
        }

        return result;
    }

//    private User getLoginUser() {
//        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//    }
}
