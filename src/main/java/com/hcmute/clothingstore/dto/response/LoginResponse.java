package com.hcmute.clothingstore.dto.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class LoginResponse {
    @JsonProperty("access_token")
    private String accessToken;

    private String refreshToken;

    private ResponseUser user;
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class ResponseUser{
        private Long id;
        private String email;

        private String firstName;

        private String lastName;

        private boolean activated;

        private RoleOfUser role;
    }
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Getter
    @Setter
    public static class RoleOfUser{
        private Long id;

        private String name;
    }



    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Getter
    @Setter
    public static class UserInsideToken{
        private Long id;

        private String email;
        private String firstName;
        private String lastName;


    }

}
