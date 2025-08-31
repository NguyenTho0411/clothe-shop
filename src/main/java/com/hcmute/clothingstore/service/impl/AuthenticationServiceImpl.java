package com.hcmute.clothingstore.service.impl;


import com.hcmute.clothingstore.appconstant.AppConstant;
import com.hcmute.clothingstore.dto.request.LoginDTO;
import com.hcmute.clothingstore.dto.request.RegisterDTO;
import com.hcmute.clothingstore.dto.response.LoginResponse;
import com.hcmute.clothingstore.dto.response.RegisterResponse;
import com.hcmute.clothingstore.entity.CustomerAccount;
import com.hcmute.clothingstore.entity.Point;
import com.hcmute.clothingstore.entity.Profile;
import com.hcmute.clothingstore.entity.User;
import com.hcmute.clothingstore.enumerated.Gender;
import com.hcmute.clothingstore.exception.APIException;
import com.hcmute.clothingstore.jwt.JwtUtil;
import com.hcmute.clothingstore.jwt.RandomUtil;
import com.hcmute.clothingstore.repository.RoleRepository;
import com.hcmute.clothingstore.repository.UserRepository;
import com.hcmute.clothingstore.service.interfaces.AuthenticationService;
import com.hcmute.clothingstore.service.interfaces.EmailService;
import com.hcmute.clothingstore.service.interfaces.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.time.Instant;


@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JwtUtil jwtUtil;


    @Autowired
    private RoleRepository roleRepository;
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

    @Override
    public RegisterResponse register(RegisterDTO user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new APIException("User with email has already existed!");
        }

        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        newUser.setRole(roleRepository.findByName(AppConstant.ROLE_USER).orElse(null));

        newUser.setActivationKey(RandomUtil.generateActivationKey());
        newUser.setActivationCode(RandomUtil.generateActivationCode());
        newUser.setActivationCodeDate(Instant.now());
        Profile profile = new Profile();
        profile.setFirstName(user.getFirstName());
        profile.setLastName(user.getLastName());
        profile.setFullName(String.format("%s %s",user.getFirstName(), user.getLastName()));
        if(user.getBirthDate() != null){
            profile.setBirthDate(user.getBirthDate());
        }
        if(user.getPhone() != null){
            profile.setPhoneNumber(user.getPhone());
        }
        if(user.getGender()!=null){
            profile.setGender(Gender.valueOf(user.getGender().toUpperCase()));
        }
        newUser.setProfile(profile);
        profile.setUser(newUser);
        CustomerAccount customerAccount = new CustomerAccount();

        Point point  = new Point();
        point.setCurrentPoints(0L);
        customerAccount.setPoint(point);
        customerAccount.setUser(newUser);
        newUser.setCustomerAccount(customerAccount);
        point.setCustomer(customerAccount);

        User savedUser = userRepository.save(newUser);
        emailService.sendActivationCode(savedUser);

        return modelMapper.map(savedUser,RegisterResponse.class);
    }

}
