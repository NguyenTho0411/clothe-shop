package com.hcmute.clothingstore.service.interfaces;

import com.hcmute.clothingstore.dto.request.UserDTO;
import com.hcmute.clothingstore.dto.response.UserResponse;
import com.hcmute.clothingstore.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;



public interface UserService {
    UserResponse createNewUser(UserDTO userDTO);


    User getUserByLogin(String email);

    void updateUserWithRefreshToken(User loginUser, String refreshToken);
}
