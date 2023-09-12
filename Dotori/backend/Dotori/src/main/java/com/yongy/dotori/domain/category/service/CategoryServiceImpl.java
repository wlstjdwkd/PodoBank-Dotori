package com.yongy.dotori.domain.category.service;

import com.yongy.dotori.domain.category.dto.CategoryDTO;
import com.yongy.dotori.domain.category.entity.Category;
import com.yongy.dotori.domain.category.repository.CategoryRepository;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryDTO> findAllCategory(){
        // 카테고리 리스트 반환
        // User user = getLoginUser();
        // TODO 테스트 후 로그인된 유저정보로 조회하도록 고칠것~
        List<Category> categories = categoryRepository.findAllByUserUserSeq(1L);
        List<CategoryDTO> result = new ArrayList<>();
        for(Category c : categories){
            result.add(CategoryDTO.builder()
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
