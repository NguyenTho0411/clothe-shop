package com.hcmute.clothingstore.dto.response;


import com.hcmute.clothingstore.enumerated.Gender;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ProfileResponse {
    private Long id;

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private String phoneNumber;

    private Gender gender;

    private String email;

    private RoleResponse roleResponse;

    private String avatar;

    private boolean hasPassword;


}
