package com.hcmute.clothingstore.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoogleUserInfoResponse {
    private String id;
    private String name;

    private String email;
    @JsonProperty("email_verified")
    private String emailVerified;

    @JsonProperty("given_name")
    private String givenName;

    @JsonProperty("family_name")
    private String familyName;

    private String picture;

    private String locale;
}
