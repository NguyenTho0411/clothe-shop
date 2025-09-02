package com.hcmute.clothingstore.dto.request.forgotpass;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestEmailRecoverDTO {
    @NotBlank(message="Please type your email!")
    private String email;
}
