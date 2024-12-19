package com.remit.recover;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class MainController {

    @GetMapping("/main.html")
    public String index(){
        return "main";
    }

    @GetMapping("/api/v3/mobile/CustomerProfile")
    public void redirectToExternalUrl1(HttpServletResponse response) throws IOException {
        response.sendRedirect("http://172.25.171.16:8082/api/v3/mobile/CustomerProfile");
    }

    @GetMapping("/api/v1/users/access-code")
    public void redirectToExternalUrl2(HttpServletResponse response) throws IOException {
        response.sendRedirect("http://172.25.171.16:8082/api/v1/users/access-code");
    }

    @GetMapping("/api/v1/mobile/ConfirmPassword")
    public void redirectToExternalUrl3(HttpServletResponse response) throws IOException {
        response.sendRedirect("http://172.25.171.16:8082/api/v1/mobile/ConfirmPassword");
    }

    @GetMapping("/api/v1/mobile/Zero/ping")
    public void redirectToExternalUrl4(HttpServletResponse response) throws IOException {
        response.sendRedirect("http://172.25.171.16:8082/api/v1/mobile/Zero/ping");
    }

    @RequestMapping("/**")
    @ResponseBody
    public ResponseEntity<String> handleUndefinedEndpoints(){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("This service is currently unavilable");
    }
}
