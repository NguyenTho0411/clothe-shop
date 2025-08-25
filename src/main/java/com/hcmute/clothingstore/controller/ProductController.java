package com.hcmute.clothingstore.controller;


import com.hcmute.clothingstore.dto.request.ProductDTO;
import com.hcmute.clothingstore.dto.response.PagninationResponse;
import com.hcmute.clothingstore.dto.response.ProductResponse;
import com.hcmute.clothingstore.entity.Product;
import com.hcmute.clothingstore.service.interfaces.ProductService;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final Logger log = LoggerFactory.getLogger(ProductController.class);


    @Autowired
    private ProductService productService;


    @PostMapping("/products")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid ProductDTO productDTO){
        ProductResponse productResponse = productService.createProduct(productDTO);
        return new ResponseEntity<>(productResponse, HttpStatus.CREATED);
    }

    @GetMapping("/products/product/{id}")
    public ResponseEntity<ProductResponse> getProductByID(@PathVariable Long id){
        ProductResponse productResponse = productService.getProductById(id);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }

    @GetMapping("/products/product/{slug}")
    public ResponseEntity<ProductResponse> getProductBySlug(@PathVariable String slug){
        ProductResponse productResponse = productService.getProductBySlug(slug);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }

    @PutMapping("/products")
    public ResponseEntity<ProductResponse> updateProduct(@RequestBody @Valid ProductDTO productDTO){
        ProductResponse productResponse = productService.update(productDTO);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }

    @GetMapping("/products")
    public ResponseEntity<PagninationResponse> getProduct(
            @RequestParam(required = false, defaultValue = "false") Boolean isBestSeller,
            @RequestParam(required = false,defaultValue = "false")  Boolean isDiscounted,
            @RequestParam(required = false) Integer days,
            @RequestParam(required = false) Double averageRating,
            @RequestParam(required = false, defaultValue = "false")  Boolean hasDiscount,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false, defaultValue = "asc") String sortOrder, @Filter Specification<Product> specification, Pageable pageable){
        PagninationResponse res = productService.getProducts(isBestSeller,isDiscounted,
                days,averageRating,hasDiscount,maxPrice,minPrice,size,sortOrder,specification,pageable);
        return new ResponseEntity<>(res,HttpStatus.OK);

    }




}
