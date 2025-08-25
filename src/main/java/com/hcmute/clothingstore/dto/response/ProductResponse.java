package com.hcmute.clothingstore.dto.response;


import com.hcmute.clothingstore.entity.ProductVariant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String desscription;
    private Double price;
    private Double minPrice;
    private Double maxPrice;
    private Double priceWithDiscount;
    private Double minPriceWithDiscount;
    private Double maxPriceWithDiscount;
    private Long categoryId;
    private String categoryName;
    private boolean isFeatured;

    private Double discountRate;
    private Double averageRating;
    private Long numberOfReviews;
    private Long numberOfSold;
    private String slug;
    private String colorDefault;

    private List<String> images;
    private List<ProductVariant> productVariants;


    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class ProductVariantResponse{
        private Long id;
        private String color;
        private String size;
        private Long quantity;

        private Long currentUserCartQuantity;

        private Double differentPrice;

        private List<String> imagesOfVariant;
    }

}
