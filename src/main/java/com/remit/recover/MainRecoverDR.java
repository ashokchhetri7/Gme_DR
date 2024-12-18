package com.remit.recover;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainRecoverDR {
    public static void main(String[] args){
        SpringApplication.run(MainRecoverDR.class, args);
    }
}


//Test URL
/*
http://localhost:8084/api/v3/mobile/CustomerProfile
http://localhost:8084/api/v1/users/access-code
http://localhost:8084/api/v1/mobile/ConfirmPassword
 */
