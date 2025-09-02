package com.hcmute.clothingstore.service.impl;


import com.hcmute.clothingstore.dto.request.CategoryDTO;
import com.hcmute.clothingstore.dto.response.CategoryResponse;
import com.hcmute.clothingstore.dto.response.PagninationResponse;
import com.hcmute.clothingstore.entity.Category;
import com.hcmute.clothingstore.exception.APIException;
import com.hcmute.clothingstore.exception.ResouceAlreadyExist;
import com.hcmute.clothingstore.exception.ResourceNotFoundException;
import com.hcmute.clothingstore.repository.CategoryRepository;
import com.hcmute.clothingstore.repository.ProductRepository;
import com.hcmute.clothingstore.service.interfaces.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ProductRepository productRepository;
    @Override
    public CategoryResponse createNewCategory(CategoryDTO categoryDTO) {
        String newCategoryName = categoryDTO.getName();
        if(categoryRepository.findByName(newCategoryName).isPresent()){
            throw new ResouceAlreadyExist("Category","categoryName",newCategoryName);
        }
        Category category = new Category();
        category.setName(newCategoryName);
        category.setImageUrl(categoryDTO.getImageUrl());
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory,CategoryResponse.class);
    }

    @Override
    public CategoryResponse updateCategory(CategoryDTO categoryDTO) {
        if(categoryDTO.getCategoryId()== null){
            throw new APIException("ID of Category can not be null");
        }
        Long id = categoryDTO.getCategoryId();
        Category category = categoryRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Category","ID",id));
        category.setName(categoryDTO.getName());
        category.setImageUrl(categoryDTO.getImageUrl());
        return modelMapper.map(categoryRepository.save(category), CategoryResponse.class);
    }

    @Override
    public CategoryResponse getCategoryByID(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Category","CategoryId",id)
        );
        return modelMapper.map(category,CategoryResponse.class);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
        ()-> new ResourceNotFoundException("Category","categoryId",id));
        category.getProducts().forEach(product -> product.setCategory(null));
        productRepository.saveAll(category.getProducts());
        categoryRepository.delete(category);
    }

    @Override
    public PagninationResponse getAllCategories(Specification<Category> spec, Pageable pageable) {
        return null;
    }

}
