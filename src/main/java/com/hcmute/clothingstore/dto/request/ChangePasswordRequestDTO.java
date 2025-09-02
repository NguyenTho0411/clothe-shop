package com.hcmute.clothingstore.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChangePasswordRequestDTO {

    @NotBlank(message = "Old password can not be empty")
    private String oldPassword;

    @NotBlank(message = "New password can not be empty")
    private String newPassword;

    @NotBlank(message = "Confirm password can not be empty")
    private String confirmPassword;
}
