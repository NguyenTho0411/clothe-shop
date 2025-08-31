package com.hcmute.clothingstore.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class I18nConfig {
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource ms = new ResourceBundleMessageSource();
        ms.setBasenames("i18n/messages");     // map tới src/main/resources/i18n/messages_*.properties
        ms.setDefaultEncoding("UTF-8");
        ms.setUseCodeAsDefaultMessage(true);  // không có key thì trả về chính code để tránh ném exception
        return ms;
    }
}