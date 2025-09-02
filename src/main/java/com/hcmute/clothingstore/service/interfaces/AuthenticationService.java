package com.hcmute.clothingstore.service.interfaces;

import com.hcmute.clothingstore.dto.request.LoginDTO;
import com.hcmute.clothingstore.dto.request.RegisterDTO;
import com.hcmute.clothingstore.dto.response.LoginResponse;
import com.hcmute.clothingstore.dto.response.RegisterResponse;

public interface AuthenticationService {
    LoginResponse login(LoginDTO loginDTO);

    RegisterResponse register(RegisterDTO registerDTO);

    LoginResponse authenticationLoginGoole(String code);

    void recoverPassword(String email);

    void resetPassword(String key, String newPassword, String confirmPassword);

    LoginResponse activateAccount(String key);

    void sendActivationEmail(String email);

    LoginResponse refreshToken(String refreshToken);

    void logout(String s);
}
