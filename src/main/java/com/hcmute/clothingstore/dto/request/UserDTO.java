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
public class UserDTO {
    private Long id;

    @Pattern(regexp = "/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$/", message = "Unvalid Email")
    @NotBlank(message = "Email must be required")
    private String email;

    @NotBlank(message = "First Name must be required")
    private String firstName;

    @NotBlank(message = "Last name must be required")
    private String lastName;

    @Pattern(regexp = "(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$\"\n", message = "Unvalid Password")
    @NotBlank(message = "Message must be required")
    private String password;

    private LocalDate birthDate;

    @Pattern(regexp = "/\\(?([0-9]{3})\\)?([ .-]?)([0-9]{3})\\2([0-9]{4})/", message = "Unvalid Phone Number ")
    @NotBlank(message = "Phone Number not blank")
    private String phoneNumber;

    @EnumValue(enumClass = Gender.class, message = "Gender invalid")
    private String gender;

    private Long roleId;

}
