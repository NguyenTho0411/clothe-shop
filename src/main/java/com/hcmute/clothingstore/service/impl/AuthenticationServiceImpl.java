package com.hcmute.clothingstore.service.impl;


import com.hcmute.clothingstore.appconstant.AppConstant;
import com.hcmute.clothingstore.dto.request.LoginDTO;
import com.hcmute.clothingstore.dto.request.RegisterDTO;
import com.hcmute.clothingstore.dto.response.GoogleTokenResponse;
import com.hcmute.clothingstore.dto.response.GoogleUserInfoResponse;
import com.hcmute.clothingstore.dto.response.LoginResponse;
import com.hcmute.clothingstore.dto.response.RegisterResponse;
import com.hcmute.clothingstore.entity.*;
import com.hcmute.clothingstore.enumerated.Gender;
import com.hcmute.clothingstore.exception.APIException;
import com.hcmute.clothingstore.exception.ResourceNotFoundException;
import com.hcmute.clothingstore.jwt.JwtUtil;
import com.hcmute.clothingstore.jwt.RandomUtil;
import com.hcmute.clothingstore.repository.RoleRepository;
import com.hcmute.clothingstore.repository.TokenBlackListRepository;
import com.hcmute.clothingstore.repository.UserRepository;
import com.hcmute.clothingstore.service.interfaces.AuthenticationService;
import com.hcmute.clothingstore.service.interfaces.EmailService;
import com.hcmute.clothingstore.service.interfaces.GoogleAuthenticationService;
import com.hcmute.clothingstore.service.interfaces.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;


@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GoogleAuthenticationService googleAuthenticationService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenBlackListRepository tokenBlacklistRepo;


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
        point.setTotalAccumulatedPoints(0l);
        customerAccount.setPoint(point);
        customerAccount.setUser(newUser);
        newUser.setCustomerAccount(customerAccount);
        point.setCustomer(customerAccount);

        User savedUser = userRepository.save(newUser);
        emailService.sendActivationCode(savedUser);

        return modelMapper.map(savedUser,RegisterResponse.class);
    }

    @Override
    public LoginResponse authenticationLoginGoole(String code) {
        GoogleTokenResponse googleTokenResponse = googleAuthenticationService.getTokenFromCode(code);

        GoogleUserInfoResponse userInfo =googleAuthenticationService.getUserInfo(googleTokenResponse.getAccessToken());

        User user = findOrCreateGoogleUser(userInfo);
        LoginResponse loginResponse =convertUserToLoginResponse(user);
        String accessToken = jwtUtil.createAccessToken(user, loginResponse);
        String refreshToken = jwtUtil.creteRefreshToken(user.getEmail(),loginResponse);
        userService.updateUserWithRefreshToken(user,refreshToken);
        return loginResponse;
    }

    private LoginResponse convertUserToLoginResponse(User user) {
        LoginResponse.ResponseUser responseUser =new LoginResponse.ResponseUser();
        responseUser.setEmail(user.getEmail());
        responseUser.setId(user.getId());
        if(user.getProfile() != null){
            responseUser.setFirstName(user.getProfile().getFirstName());
            responseUser.setLastName(user.getProfile().getLastName());
        }
        responseUser.setActivated(user.isActivated());
        LoginResponse.RoleOfUser roleOfUser = new LoginResponse.RoleOfUser();
        if(user.getRole()!= null){
            roleOfUser.setId(user.getRole().getId());
            roleOfUser.setName(user.getRole().getName());
            responseUser.setRole(roleOfUser);
        }
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUser(responseUser);
        return loginResponse;
    }

    private User findOrCreateGoogleUser(GoogleUserInfoResponse userInfo) {
        Optional<User> existingUser = userRepository.findByEmail(userInfo.getEmail());
        if(existingUser.isPresent()){
            User user =existingUser.get();
            user.setGoogleId(userInfo.getId());
            return userRepository.save(user);
        }else{
            User user = new User();
            user.setActivated(true);
            user.setGoogleId(userInfo.getId());
            user.setEmail(userInfo.getEmail());
            user.setRole(roleRepository.findByName(AppConstant.ROLE_USER).
                    orElseThrow(() -> new ResourceNotFoundException("Role","Name",AppConstant.ROLE_USER)));

            Profile profile = new Profile();
            profile.setFirstName(userInfo.getGivenName());
            profile.setLastName(userInfo.getFamilyName());
            profile.setFullName(userInfo.getName());
            profile.setAvatar(userInfo.getPicture());
            user.setProfile(profile);
            profile.setUser(user);
            CustomerAccount customerAccount = new CustomerAccount();
            Point point = new Point();
            point.setCurrentPoints(0L);
            point.setTotalAccumulatedPoints(0L);
            customerAccount.setUser(user);
            customerAccount.setPoint(point);
            user.setCustomerAccount(customerAccount);
            point.setCustomer(customerAccount);
            return userRepository.save(user);
        }
    }

    @Override
    public void recoverPassword(String email) {
        User user = userRepository.findByEmailAndActivatedTrue(email)
                .orElseThrow(() -> new APIException("User with email: "+email+" is not existed!"));
        if(user.getResetDate() != null && user.getResetDate().isAfter(Instant.now().minusSeconds(30))){
            throw new APIException("You request frequently! Please try after 30s!");
        };

        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        userRepository.save(user);
        emailService.sendRecoverPasswordEmail(user);
    }

    @Override
    public void resetPassword(String key, String newPassword, String confirmPassword) {
        User user = userRepository.findByResetKey(key).orElseThrow(()-> new APIException("Reset Token is invalid"));

        if(user.getResetDate().isBefore(Instant.now().minusSeconds(60*15))){
            throw new APIException("Reset token is invalid");
        }
        if(!newPassword.equals(confirmPassword)){
            throw new APIException("Your Confirm password is not match with new password");
        }
        user.setResetDate(null);
        user.setResetKey(null);
        userRepository.save(user);
    }

    @Override
    public void logout(String refreshToken) {
        String token = jwtUtil.getCurrentUserJWT().orElse(null);
        if(token == null){
            throw new APIException("Invalid Access Token");
        }
        try{
            jwtUtil.jwtDecoder(refreshToken);
        }catch (Exception e){
            throw new APIException("Invalid refresh token");
        }
        Jwt jwt = jwtUtil.jwtDecoder(token);
        TokenBlacklist tokenBlacklist = new TokenBlacklist();
        tokenBlacklist.setToken(token);
        tokenBlacklist.setCreatedDate(jwt.getIssuedAt());
        tokenBlacklist.setExpiryDate(jwt.getExpiresAt());
        tokenBlacklistRepo.save(tokenBlacklist);

        String email = jwtUtil.getCurrentUserLogin().orElse(null);
        if(email!= null){
            User user = userService.handleGetUserByUsername(email);
            user.getUserRefreshTokens().removeIf(rt -> rt.getRefreshToken().equals(refreshToken));
            userRepository.save(user);
        }

    }

    @Override
    public LoginResponse activateAccount(String key) {
        if(userRepository.findByActivationKey(key).isPresent()){
            User user = userRepository.findByActivationKeyWithLock(key).orElseThrow(()-> new ResourceNotFoundException("User","Activation Key",key));
            user.setActivated(true);
            user.setActivationKey(null);
            User savedUser = userRepository.save(user);
            LoginResponse loginResponse = convertUserToLoginResponse(user);

            if(savedUser.isActivated()){
                String accessToken = jwtUtil.createAccessToken(savedUser,loginResponse);
                loginResponse.setAccessToken(accessToken);
                String refreshToken = jwtUtil.creteRefreshToken(savedUser.getEmail(),loginResponse);
                loginResponse.setRefreshToken(refreshToken);
                userService.updateUserWithRefreshToken(savedUser,refreshToken);
            }
            return loginResponse;
        }
        else{
            throw new RuntimeException("Your token is invalid");
        }
    }

    @Override
    public void sendActivationEmail(String email) {
        if(userRepository.findByEmailAndActivatedFalse(email).isPresent()){
            User user = userRepository.findByEmail(email).get();
            emailService.sendActivationCode(user);
        }else{
            throw new ResourceNotFoundException("User","Email",email);
        }
    }

    @Override
    public LoginResponse refreshToken(String refreshToken) {
        Jwt jwt =jwtUtil.jwtDecoder(refreshToken);
        String email = jwt.getSubject();
        User user = userRepository.findByEmail(email).orElseThrow(()->
                new ResourceNotFoundException("User","email",email));

        Boolean validateRefreshToken = user.getUserRefreshTokens().stream().anyMatch(rt ->
                rt.getRefreshToken().equals(refreshToken) && rt.getExpiryDate().isAfter(Instant.now()));

        if(!validateRefreshToken){
            throw new APIException("Invalid Token");
        }
        LoginResponse loginResponse = convertUserToLoginResponse(user);
        String accessToken = jwtUtil.createAccessToken(user,loginResponse);
        loginResponse.setAccessToken(accessToken);
        String newRefreshToken = jwtUtil.creteRefreshToken(user.getEmail(),loginResponse);
        loginResponse.setRefreshToken(newRefreshToken);
        user.getUserRefreshTokens().removeIf(rt -> rt.getRefreshToken().equals(refreshToken));
        userService.updateUserWithRefreshToken(user,newRefreshToken);
        return loginResponse;
    }
}
