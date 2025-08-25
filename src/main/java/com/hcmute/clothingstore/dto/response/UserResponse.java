package com.hcmute.clothingstore.dto.response;


import com.hcmute.clothingstore.enumerated.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;

    private String email;


    private String firstName;

    private String lastName;

    private String fullName;

    private String phoneNumber;

    private LocalDate birthDate;

    private Gender gender;

    private RoleResponse role;
}
