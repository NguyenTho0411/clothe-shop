package com.hcmute.clothingstore.service.interfaces;

import com.hcmute.clothingstore.dto.request.LoginDTO;
import com.hcmute.clothingstore.dto.response.LoginResponse;

public interface AuthenticationService {
    LoginResponse login(LoginDTO loginDTO);
}
