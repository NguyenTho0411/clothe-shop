package com.hcmute.clothingstore.service.interfaces;

import com.hcmute.clothingstore.dto.response.GoogleTokenResponse;
import com.hcmute.clothingstore.dto.response.GoogleUserInfoResponse;
import org.springframework.beans.factory.annotation.Value;

public interface GoogleAuthenticationService {
    GoogleTokenResponse getTokenFromCode(String code);

    GoogleUserInfoResponse getUserInfo(String accessToken);

}
