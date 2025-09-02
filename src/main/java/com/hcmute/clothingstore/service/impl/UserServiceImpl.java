package com.hcmute.clothingstore.service.impl;

import com.hcmute.clothingstore.dto.request.ChangePasswordRequestDTO;
import com.hcmute.clothingstore.dto.request.EditProfileRequestDTO;
import com.hcmute.clothingstore.dto.request.UserDTO;
import com.hcmute.clothingstore.dto.response.ProfileResponse;
import com.hcmute.clothingstore.dto.response.RoleResponse;
import com.hcmute.clothingstore.dto.response.UserResponse;
import com.hcmute.clothingstore.entity.Profile;
import com.hcmute.clothingstore.entity.Role;
import com.hcmute.clothingstore.entity.User;
import com.hcmute.clothingstore.entity.UserRefreshToken;
import com.hcmute.clothingstore.enumerated.Gender;
import com.hcmute.clothingstore.exception.APIException;
import com.hcmute.clothingstore.exception.ResourceNotFoundException;
import com.hcmute.clothingstore.jwt.JwtUtil;
import com.hcmute.clothingstore.repository.RoleRepository;
import com.hcmute.clothingstore.repository.UserRepository;
import com.hcmute.clothingstore.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public UserResponse createNewUser(UserDTO userDTO) {
        String email = userDTO.getEmail();
        if(userRepository.findByEmail(email) != null){
            throw new APIException("User with email "+email+" has already existed!");
        }
        Long roleId = userDTO.getId();
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new ResourceNotFoundException("Role","roleId",roleId));

        User user = new User();
        user.setActivated(true);
        user.setEmail(userDTO.getEmail());
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        Profile profile = new Profile();
        String firstName = userDTO.getFirstName();
        String lastName = userDTO.getLastName();
        profile.setFirstName(firstName);
        profile.setLastName(lastName);
        profile.setFullName(String.format("%s %s",firstName,lastName));
        profile.setPhoneNumber(userDTO.getPhoneNumber());
        profile.setGender(userDTO.getGender() != null ? Gender.valueOf(userDTO.getGender().toUpperCase()) :null);
        profile.setBirthDate(userDTO.getBirthDate() != null ? userDTO.getBirthDate(): null);

        profile.setUser(user);
        user.setProfile(profile);


        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserResponse.class);
    }

    @Override
    public User getUserByLogin(String email) {
        return userRepository.findByEmailAndActivatedTrue(email).orElseThrow(() -> new ResourceNotFoundException("User","Email: ",email));
    }

    @Override
    public void updateUserWithRefreshToken(User loginUser, String refreshToken) {
        Jwt jwt = jwtUtil.jwtDecoder(refreshToken);

        UserRefreshToken userRefreshToken = new UserRefreshToken();
        userRefreshToken.setRefreshToken(refreshToken);
        userRefreshToken.setCreatedDate(jwt.getIssuedAt());
        userRefreshToken.setExpiryDate(jwt.getExpiresAt());
        userRefreshToken.setUser(loginUser);

        if(loginUser.getUserRefreshTokens() == null){
            loginUser.setUserRefreshTokens(new ArrayList<>());
        }
        loginUser.getUserRefreshTokens().add(userRefreshToken);
        userRepository.save(loginUser);
    }

    @Override
    public User handleGetUserByUsername(String email) {
        if(userRepository.findByEmail(email).isPresent()){
            return userRepository.findByEmailAndActivatedTrue(email).get();
        }
        return null;
    }

    @Override
    public ProfileResponse getProfile() {
        String emailCurrentUser = jwtUtil.getCurrentUserLogin().get();
        User user = userRepository.findByEmail(emailCurrentUser).orElseThrow(() -> new ResourceNotFoundException("User","email",emailCurrentUser));
        ProfileResponse profileResponse = modelMapper.map(user.getProfile(),ProfileResponse.class);
        profileResponse.setHasPassword(checkUserHasPassword(user));
        profileResponse.setEmail(user.getEmail());
        profileResponse.setRoleResponse(RoleResponse.builder().id(user.getRole().getId()).name(user.getRole().getName()).build());
        return profileResponse;

    }

    private boolean checkUserHasPassword(User user) {
        if(user.getGoogleId() != null && (user.getPassword() == null || user.getPassword().isEmpty())){
            return false;
        }
        return true;
    }

    @Override
    public UserResponse editProfile(EditProfileRequestDTO requestDTO) {
        String emailCurrentUser = jwtUtil.getCurrentUserLogin().get();
        User user = userRepository.findByEmail(emailCurrentUser).orElseThrow(() -> new ResourceNotFoundException("User","Email", emailCurrentUser));

        Profile profile = user.getProfile();
        profile.setFirstName(requestDTO.getFirstName());
        profile.setLastName(requestDTO.getLastName());
        profile.setFullName(String.format("%s %s", requestDTO.getFirstName(), requestDTO.getLastName()));
        profile.setBirthDate(requestDTO.getBirthDate());
        profile.setPhoneNumber(requestDTO.getPhoneNumber());
        profile.setGender(requestDTO.getGender() != null ? Gender.valueOf(requestDTO.getGender().toUpperCase()):null);
        userRepository.save(user);

        return UserResponse.builder().id(user.getId()).firstName(profile.getFirstName())
                .lastName(profile.getLastName()).birthDate(profile.getBirthDate())
                .phoneNumber(profile.getPhoneNumber()).gender(profile.getGender()).email(user.getEmail())
                .build();
    }

    @Override
    public void changePassword(ChangePasswordRequestDTO changePasswordRequestDTO) {
        String emailCurrentUser = jwtUtil.getCurrentUserLogin().get();
        User user = userRepository.findByEmail(emailCurrentUser).orElseThrow(()-> new ResourceNotFoundException("User","Email",emailCurrentUser));

        if(!passwordEncoder.matches(changePasswordRequestDTO.getOldPassword(),user.getPassword())){
            throw new APIException("OLD Password is not correct!");
        }
        if(!changePasswordRequestDTO.getNewPassword().equals(changePasswordRequestDTO.getConfirmPassword())){
            throw new APIException("Confirm password and new password is not match");
        }
        user.setPassword(passwordEncoder.encode(changePasswordRequestDTO.getNewPassword()));
        userRepository.save(user);
    }
}
