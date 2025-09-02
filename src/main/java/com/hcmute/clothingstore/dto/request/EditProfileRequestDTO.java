package com.hcmute.clothingstore.dto.request;


import com.hcmute.clothingstore.config.EnumValue;
import com.hcmute.clothingstore.enumerated.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditProfileRequestDTO {

    @NotBlank(message = "First name can not be empty")
    private String firstName;

    @NotBlank(message = "Last Name can not be empty")
    private String lastName;

    private LocalDate birthDate;

    @Pattern(regexp = "^(\\+84|0)\\d{9,10}$", message = "Invalid Phone Number")
    private String phoneNumber;

    @EnumValue(enumClass = Gender.class,message = "Invalid gender")
    private String gender;
}
