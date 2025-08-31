package com.hcmute.clothingstore.service.impl;

import com.hcmute.clothingstore.dto.request.UserDTO;
import com.hcmute.clothingstore.dto.response.UserResponse;
import com.hcmute.clothingstore.entity.Profile;
import com.hcmute.clothingstore.entity.Role;
import com.hcmute.clothingstore.entity.User;
import com.hcmute.clothingstore.enumerated.Gender;
import com.hcmute.clothingstore.exception.APIException;
import com.hcmute.clothingstore.exception.ResourceNotFoundException;
import com.hcmute.clothingstore.repository.RoleRepository;
import com.hcmute.clothingstore.repository.UserRepository;
import com.hcmute.clothingstore.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    }
}
