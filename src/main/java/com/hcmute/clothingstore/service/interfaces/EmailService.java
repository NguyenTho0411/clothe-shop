package com.hcmute.clothingstore.service.interfaces;

import com.hcmute.clothingstore.entity.User;

public interface EmailService {
    void sendActivationCode(User savedUser);
}
