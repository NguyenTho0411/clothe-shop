package com.hcmute.clothingstore.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GoogleAuthenticationRequestDTO {

    @NotBlank(message = "The code should be not blank")
    private String code;
}
