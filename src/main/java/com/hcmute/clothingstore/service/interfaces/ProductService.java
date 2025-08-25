package com.hcmute.clothingstore.service.interfaces;

import com.hcmute.clothingstore.dto.request.ProductDTO;
import com.hcmute.clothingstore.dto.response.PagninationResponse;
import com.hcmute.clothingstore.dto.response.ProductResponse;
import com.hcmute.clothingstore.entity.Product;
import com.hcmute.clothingstore.entity.ProductVariant;
import org.springframework.data.jpa.domain.Specification;

import java.awt.print.Pageable;

public interface ProductService {
    ProductResponse createProduct(ProductDTO productDTO);
    String createSlug(String input);
    String generateMeaningfulSku(ProductVariant variant);

    ProductResponse getProductById(Long id);

    ProductResponse getProductBySlug(String slug);

    ProductResponse update(ProductDTO productDTO);



    PagninationResponse getProducts(Boolean isBestSeller, Boolean isDiscounted, Integer days, Double averageRating, Boolean hasDiscount, Double maxPrice, Double minPrice, String size, String sortOrder, Specification<Product> specification, Pageable pageable);
}
