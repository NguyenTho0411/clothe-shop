package com.hcmute.clothingstore.dto.request;


import com.hcmute.clothingstore.config.EnumValue;
import com.hcmute.clothingstore.entity.ProductVariant;
import com.hcmute.clothingstore.enumerated.EColor;
import com.hcmute.clothingstore.enumerated.ESize;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDTO {

    private Long id;

    @NotBlank(message = "Name of product must be not empty")
    private String name;

    @NotBlank(message = "Description of product must be not empty")
    private String description;

    @NotNull(message = "Price of product must be not empty")
    @Min(value=0, message = "The minimum of product is not 0")
    private Double price;

    @NotNull(message = "Category of product must be not null")
    private Long categoryId;

    private Boolean featured = false;

    @NotNull(message = "Images of product must be required")
    private List<String> images;

    @Valid
    private List<ProductVariantDTO> variants;

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductVariantDTO{

        private Long id;

        @EnumValue(enumClass = EColor.class, message = "Invalid Color")
        private String color;

        @EnumValue(enumClass = ESize.class, message = "Invalud Size")
        private String size;

        @Min(value = 0, message = "You must type quantity greater than 0")
        private Integer quantity;

        @NotNull(message = "You must type the price of this product variants")
        private Double differentPrice;

        @NotEmpty(message = "Variant image must not be empty")
        private List<String> images;

    }


}
