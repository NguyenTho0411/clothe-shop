package com.hcmute.clothingstore.controller;


import com.hcmute.clothingstore.dto.request.CategoryDTO;
import com.hcmute.clothingstore.dto.response.CategoryResponse;
import com.hcmute.clothingstore.dto.response.PagninationResponse;
import com.hcmute.clothingstore.entity.Category;
import com.hcmute.clothingstore.service.interfaces.CategoryService;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryController {
    private final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryservice;

    @PostMapping("/categories")
    public ResponseEntity<CategoryResponse> createNewCategory(@RequestBody @Valid CategoryDTO categoryDTO){
        CategoryResponse categoryResponse =categoryservice.createNewCategory(categoryDTO);
        return new ResponseEntity<>(categoryResponse, HttpStatus.CREATED);
    }

    @PutMapping("/categories")
    public ResponseEntity<CategoryResponse> updateCategory(@RequestBody @Valid CategoryDTO categoryDTO){
        CategoryResponse categoryResponse = categoryservice.updateCategory(categoryDTO);
        return new ResponseEntity<>(categoryResponse,HttpStatus.OK);
    }
    @GetMapping("/categories")
    public ResponseEntity<PagninationResponse> getAllCategory(@Filter Specification<Category> spec, Pageable pageable){
        PagninationResponse response = categoryservice.getAllCategories(spec, pageable);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @GetMapping("/categories/{id}")
    public ResponseEntity<CategoryResponse> getCategoryByID(@PathVariable Long id){
        CategoryResponse categoryResponse =  categoryservice.getCategoryByID(id);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id){
        categoryservice.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
