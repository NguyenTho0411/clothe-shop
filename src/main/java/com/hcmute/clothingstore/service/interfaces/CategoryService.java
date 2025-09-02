package com.hcmute.clothingstore.service.interfaces;

import com.hcmute.clothingstore.dto.request.CategoryDTO;
import com.hcmute.clothingstore.dto.response.CategoryResponse;
import com.hcmute.clothingstore.dto.response.PagninationResponse;
import com.hcmute.clothingstore.entity.Category;
import org.springframework.data.jpa.domain.Specification;

import java.awt.print.Pageable;

public interface CategoryService {
    CategoryResponse createNewCategory(CategoryDTO categoryDTO);

    CategoryResponse updateCategory(CategoryDTO categoryDTO);

    PagninationResponse getAllCategories(Specification<Category> spec, Pageable pageable);

    CategoryResponse getCategoryByID(Long id);

    void deleteCategory(Long id);
}
