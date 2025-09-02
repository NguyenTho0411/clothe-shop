package com.hcmute.clothingstore.service.interfaces;

import com.hcmute.clothingstore.dto.request.ChangePasswordRequestDTO;
import com.hcmute.clothingstore.dto.request.EditProfileRequestDTO;
import com.hcmute.clothingstore.dto.request.UserDTO;
import com.hcmute.clothingstore.dto.response.ProfileResponse;
import com.hcmute.clothingstore.dto.response.UserResponse;
import com.hcmute.clothingstore.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;



public interface UserService {
    UserResponse createNewUser(UserDTO userDTO);


    User getUserByLogin(String email);

    void updateUserWithRefreshToken(User loginUser, String refreshToken);

    User handleGetUserByUsername(String email);

    ProfileResponse getProfile();

    UserResponse editProfile(EditProfileRequestDTO requestDTO);

    void changePassword(ChangePasswordRequestDTO changePasswordRequestDTO);
}
