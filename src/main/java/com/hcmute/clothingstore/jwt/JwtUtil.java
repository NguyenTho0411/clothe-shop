package com.hcmute.clothingstore.jwt;


import com.hcmute.clothingstore.dto.response.LoginResponse;
import com.hcmute.clothingstore.entity.User;
import com.hcmute.clothingstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class JwtUtil {


    @Value("${jwt-base-64-secret-key}")
    private String jwtKey;

    @Value("${jwt.access-token-validity-in-seconds}")
    private Long accessTokenExpiration;

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private Long refreshTokenExpiration;

    public static final MacAlgorithm JWT_ALGORITHM =MacAlgorithm.HS512;

    private JwtEncoder jwtEncoder;
    private UserRepository userRepository;


    public String creteRefreshToken(String email, LoginResponse loginResponse) {
        LoginResponse.UserInsideToken userInsideToken = convertLoginResponseIntoUserInsideToken(loginResponse);
        Instant now = Instant.now();

        JwtClaimsSet claimsSet = JwtClaimsSet.builder().
                issuedAt(now).
                expiresAt(now.plus(this.refreshTokenExpiration,ChronoUnit.SECONDS)).
                subject(email).
                claim("user",userInsideToken).
                build();
        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader,claimsSet)).getTokenValue();
    }

    private String buidScope(User user) {
        StringBuilder scope = new StringBuilder();
        if(user.getRole()!=null){
            scope.append("ROLE_").append(user.getRole().getName());
        }
        return scope.toString();
    }

    public LoginResponse.UserInsideToken convertLoginResponseIntoUserInsideToken(LoginResponse loginResponse) {
        LoginResponse.UserInsideToken userInsideToken = new LoginResponse.UserInsideToken();
        userInsideToken.setId(loginResponse.getUser().getId());
        userInsideToken.setFirstName(loginResponse.getUser().getFirstName());
        userInsideToken.setLastName(loginResponse.getUser().getLastName());
        userInsideToken.setEmail(loginResponse.getUser().getEmail());
        return userInsideToken;
    }

    public String createAccessToken(User user, LoginResponse loginResponse) {
        LoginResponse.UserInsideToken userInsideToken = convertLoginResponseIntoUserInsideToken(loginResponse);
        Instant now = Instant.now();

        JwtClaimsSet claimsSet = JwtClaimsSet.builder().
                issuedAt(Instant.now())
                .expiresAt(now.plus(this.accessTokenExpiration, ChronoUnit.SECONDS))
                .subject(loginResponse.getUser().getEmail())
                .claim("scope",buidScope(user)).
                claim("user",userInsideToken).build();
        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader,claimsSet)).getTokenValue();
    }
}
