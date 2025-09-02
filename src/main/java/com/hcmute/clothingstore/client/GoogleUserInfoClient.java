package com.hcmute.clothingstore.client;


import com.hcmute.clothingstore.dto.response.GoogleUserInfoResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="googleUserInfoClient", url = "${google.userinfo.base-url}")
public interface GoogleUserInfoClient {

    @GetMapping
    GoogleUserInfoResponse getUserInfo(@RequestParam("access_token") String accessToken);
}
