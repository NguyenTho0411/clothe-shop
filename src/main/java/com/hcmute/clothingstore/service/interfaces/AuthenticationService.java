package com.hcmute.clothingstore.service.interfaces;

import com.hcmute.clothingstore.dto.request.LoginDTO;
import com.hcmute.clothingstore.dto.request.RegisterDTO;
import com.hcmute.clothingstore.dto.response.LoginResponse;
import com.hcmute.clothingstore.dto.response.RegisterResponse;

public interface AuthenticationService {
    LoginResponse login(LoginDTO loginDTO);

    RegisterResponse register(RegisterDTO registerDTO);
}
