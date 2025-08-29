package com.hcmute.clothingstore.dto.request;


import com.hcmute.clothingstore.config.EnumValue;
import com.hcmute.clothingstore.enumerated.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {

    @NotBlank(message = "Please type email field")
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Please type valid email")
    private String email;

    @NotBlank(message = "Please type password field")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$", message = "Please type valid password")
    @Size(min=6)
    private String password;

    @NotBlank(message = "Please type first name field")
    private String firstName;

    @NotBlank(message = "Pleaswe type last name field")
    private String lastName;

    @Pattern(regexp = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$", message = "Please type valid phone number")
    private String phone;


    private LocalDate birthDate;

    @EnumValue(enumClass = com.hcmute.clothingstore.enumerated.Gender.class, message = "Invalid Gender")
    private String Gender;

}
