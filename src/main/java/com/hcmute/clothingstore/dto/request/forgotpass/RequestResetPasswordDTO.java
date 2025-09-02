package com.hcmute.clothingstore.dto.request.forgotpass;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestResetPasswordDTO {

    @NotBlank(message = "Password must be not blank")
    private String newPassword;

    @NotBlank(message = "Confirm password must be not blank")
    private String confirmPassword;
}
