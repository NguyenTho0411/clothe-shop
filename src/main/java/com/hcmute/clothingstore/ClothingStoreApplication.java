package com.hcmute.clothingstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients(basePackages = "com.hcmute.clothingstore.client")
public class ClothingStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClothingStoreApplication.class, args);
    }

}
