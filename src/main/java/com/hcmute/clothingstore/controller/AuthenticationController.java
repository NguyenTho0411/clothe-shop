package com.hcmute.clothingstore.controller;


import com.hcmute.clothingstore.appconstant.AppConstant;
import com.hcmute.clothingstore.dto.request.LoginDTO;
import com.hcmute.clothingstore.dto.request.RegisterDTO;
import com.hcmute.clothingstore.dto.response.LoginResponse;
import com.hcmute.clothingstore.dto.response.RegisterResponse;
import com.hcmute.clothingstore.jwt.JwtUtil;
import com.hcmute.clothingstore.service.interfaces.AuthenticationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/api")
public class AuthenticationController {

    public static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private JwtUtil jwtUtil;

    private AuthenticationManager authenticationManager;


            @PostMapping("/auth/login")
    private ResponseEntity<?> loginUser(@RequestBody @Valid LoginDTO loginDTO){
        Authentication authentication;
        try{
            authentication =authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(),loginDTO.getPassword()));
        }catch (AuthenticationException e){
          Map<String,Object> map = new HashMap<>();
          map.put("Message","Bad Credential");
          map.put("status",false);
          return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
        }
        LoginResponse loginResponse = authenticationService.login(loginDTO);
        if(loginResponse.getUser().isActivated()){
            String refreshToken =jwtUtil.creteRefreshToken(loginDTO.getEmail(),loginResponse);

            ResponseCookie responseCookie = ResponseCookie.from(AppConstant.REFRESH_TOKEN_COOKIE_NAME,refreshToken)
                    .httpOnly(true).secure(true).path("/").maxAge(AppConstant.REFRESH_TOKEN_COOKIE_EXPIRE).sameSite("NONE").build();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, responseCookie.toString());
            return new ResponseEntity<>(loginResponse,headers,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(loginResponse,HttpStatus.OK);
        }
    }

    @PostMapping("/auth/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterDTO registerDTO){
        RegisterResponse registerResponse = authenticationService.register(registerDTO);
        return new ResponseEntity<>(registerResponse,HttpStatus.CREATED);
    }



}
