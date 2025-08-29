package com.hcmute.clothingstore.service.impl;

import com.hcmute.clothingstore.AppConstant;
import com.hcmute.clothingstore.dto.request.LoginDTO;
import com.hcmute.clothingstore.dto.response.LoginResponse;
import com.hcmute.clothingstore.entity.User;
import com.hcmute.clothingstore.exception.APIException;
import com.hcmute.clothingstore.jwt.JwtUtil;
import com.hcmute.clothingstore.repository.UserRepository;
import com.hcmute.clothingstore.service.interfaces.AuthenticationService;
import com.hcmute.clothingstore.service.interfaces.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public LoginResponse login(LoginDTO loginDTO) {
        User loginUser = userService.getUserByLogin(loginDTO.getEmail());
        if(!AppConstant.ROLE_USER.equalsIgnoreCase(loginUser.getRole().getName())){
            throw new APIException("User name or password is not valid");
        }
        LoginResponse loginResponse = modelMapper.map(loginUser,LoginResponse.class);
        if(loginUser.isActivated()){
            String accessToken = jwtUtil.createAccessToken(loginUser, loginResponse);
            loginResponse.setAccessToken(accessToken);

            String refreshToken = jwtUtil.creteRefreshToken(loginUser.getEmail(), loginResponse);
            loginResponse.setRefreshToken(refreshToken);

            userService.updateUserWithRefreshToken(loginUser,refreshToken);
        }

        return loginResponse;
    }
}
