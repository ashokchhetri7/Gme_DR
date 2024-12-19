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
http://localhost:8084/api/v1/mobile/Zero/ping
http://localhost:8084/static/main.html
http://localhost:8084/main.html
http://localhost:8084/randomurl

http://localhost:8084/api/v3/mobile/ocr/getDropDownRelatedData/bank

https://drmobileapi.gmeremit.com:8002/api/v3/mobile/CustomerProfile
https://drmobileapi.gmeremit.com:8002/api/v1/users/access-code
https://drmobileapi.gmeremit.com:8002/api/v1/mobile/ConfirmPassword

 */
