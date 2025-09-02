package com.hcmute.clothingstore.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {

    private Long categoryId;
    @NotBlank(message = "Field name must be not empty")
    private String name;
    @NotBlank(message= "Image Url field must be not empty")
    private String imageUrl;
}
