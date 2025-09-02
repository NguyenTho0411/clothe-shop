package com.hcmute.clothingstore.service.impl;

import com.hcmute.clothingstore.client.GoogleAuthClient;
import com.hcmute.clothingstore.client.GoogleUserInfoClient;
import com.hcmute.clothingstore.dto.response.GoogleTokenResponse;
import com.hcmute.clothingstore.dto.response.GoogleUserInfoResponse;
import com.hcmute.clothingstore.service.interfaces.GoogleAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class GoogleAuthenticationServiceImpl implements GoogleAuthenticationService {
    private final Logger log = LoggerFactory.getLogger(GoogleAuthenticationServiceImpl.class);

    private final GoogleAuthClient googleAuthClient;

    private final GoogleUserInfoClient googleUserInfoClient;

    @Value("${google.auth.client-id}")
    private String clientId;
    @Value("${google.auth.client-secret}")
    private String clientSecret;
    @Value("${google.auth.redirect-uri}")
    private String redirectUri;
    @Override
    public GoogleTokenResponse getTokenFromCode(String authorizationCode) {
        try{
            String decodedCode = URLDecoder.decode(authorizationCode, StandardCharsets.UTF_8.toString());
            Map<String,String> formParam = new HashMap<>();
            formParam.put("code",decodedCode);
            formParam.put("client_id",clientId);
            formParam.put("client_secret",clientSecret);
            formParam.put("redirect_uri",redirectUri);
            formParam.put("grant_type","authorization_code");
            return googleAuthClient.getTokenFromCode(formParam);

        }catch (UnsupportedEncodingException e){
throw new RuntimeException("Error decoding authorization code",e);
        }
    }

    @Override
    public GoogleUserInfoResponse getUserInfo(String accessToken) {
        return googleUserInfoClient.getUserInfo(accessToken);
    }
}
