package com.hcmute.clothingstore.controller;


import com.hcmute.clothingstore.dto.request.UserDTO;
import com.hcmute.clothingstore.dto.response.UserResponse;
import com.hcmute.clothingstore.entity.User;
import com.hcmute.clothingstore.service.interfaces.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import com.turkraft.springfilter.boot.Filter;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;


    @PostMapping("/users")
    public ResponseEntity<UserResponse> createNewUser(@RequestBody @Valid UserDTO userDTO){
        UserResponse user = userService.createNewUser(userDTO);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
