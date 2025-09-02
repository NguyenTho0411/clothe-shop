package com.hcmute.clothingstore.controller;


import com.hcmute.clothingstore.appconstant.AppConstant;
import com.hcmute.clothingstore.dto.request.GoogleAuthenticationRequestDTO;
import com.hcmute.clothingstore.dto.request.LoginDTO;
import com.hcmute.clothingstore.dto.request.LogoutDTO;
import com.hcmute.clothingstore.dto.request.RegisterDTO;
import com.hcmute.clothingstore.dto.request.forgotpass.RequestEmailRecoverDTO;
import com.hcmute.clothingstore.dto.request.forgotpass.RequestResetPasswordDTO;
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
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/auth/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutDTO logoutDTO,
                    @CookieValue(name= AppConstant.REFRESH_TOKEN_COOKIE_NAME, required = false) String refreshToken){
        ResponseCookie responseCookie = ResponseCookie.from(AppConstant.REFRESH_TOKEN_COOKIE_NAME,"").sameSite("None").httpOnly(true).secure(true).path("/").maxAge(AppConstant.REFRESH_TOKEN_COOKIE_EXPIRE).build();
        authenticationService.logout(refreshToken == null ? logoutDTO.getRefreshToken() : refreshToken);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).build();
    }

    @PostMapping("/auth/login/google")
    public ResponseEntity<LoginResponse> loginGoogle(@RequestBody @Valid GoogleAuthenticationRequestDTO loginGoogleDTO){
        LoginResponse loginResponse =authenticationService.authenticationLoginGoole(loginGoogleDTO.getCode());
        String refreshToken = jwtUtil.creteRefreshToken(loginResponse.getUser().getEmail(),loginResponse);
        ResponseCookie springResponseCookie =ResponseCookie.from(AppConstant.REFRESH_TOKEN_COOKIE_NAME,refreshToken).httpOnly(true)
                .secure(true).path("/").maxAge(AppConstant.REFRESH_TOKEN_COOKIE_EXPIRE).sameSite("None").build();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE,springResponseCookie.toString());
        return new ResponseEntity<>(loginResponse,headers,HttpStatus.OK);

    }

    @PostMapping("/auth/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterDTO registerDTO){
        RegisterResponse registerResponse = authenticationService.register(registerDTO);
        return new ResponseEntity<>(registerResponse,HttpStatus.CREATED);
    }

    @PostMapping("/auth/recover_password")
    public ResponseEntity<Void> recoverPassword(@RequestBody @Valid RequestEmailRecoverDTO recoverDTO){
        authenticationService.recoverPassword(recoverDTO.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/auth/reset_password")
    public ResponseEntity<Void> resetPassword(@RequestParam("key") String key,
                                              @RequestBody @Valid RequestResetPasswordDTO resetPasswordDTO){
                authenticationService.resetPassword(key,resetPasswordDTO.getNewPassword(),resetPasswordDTO.getConfirmPassword());
                return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/auth/activate")
    public ResponseEntity<LoginResponse> activateAccount(@RequestParam("key") String key){
                LoginResponse loginResponse = authenticationService.activateAccount(key);
                return new ResponseEntity<>(loginResponse,HttpStatus.OK);
    }

    @GetMapping("/auth/send-email-activation")
    public ResponseEntity<Void> sendActivationEmail(@RequestParam("email") String email){
                authenticationService.sendActivationEmail(email);
                return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<LoginResponse> refreshToken(@CookieValue(name = AppConstant.REFRESH_TOKEN_COOKIE_NAME, required = false) String refreshTokenCookie,
                                                      @RequestParam(name = "refresh_token", required = false) String refreshTokenParam){
        String refreshToken = refreshTokenCookie != null ? refreshTokenCookie : refreshTokenParam;
        LoginResponse loginResponse = authenticationService.refreshToken(refreshToken);
        String newRefreshToken = jwtUtil.creteRefreshToken(loginResponse.getUser().getEmail(),loginResponse);
        ResponseCookie responseCookie = ResponseCookie.from(AppConstant.REFRESH_TOKEN_COOKIE_NAME,newRefreshToken).httpOnly(true)
                .path("/").secure(true).maxAge(AppConstant.REFRESH_TOKEN_COOKIE_EXPIRE).sameSite("None").build();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, responseCookie.toString());
        return new ResponseEntity<>(loginResponse,headers,HttpStatus.OK);
    }

}
