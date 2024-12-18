package com.remit.recover;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ApiController {
    @RequestMapping("/**")
    public void validateEndpoint(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestUri = request.getRequestURI();
        System.out.println("Requested URI: " + requestUri);

        // Allow static resources and explicitly allowed endpoints
        if (!ApiConfiguration.ALLOWED_ENDPOINTS.contains(requestUri) &&
                !requestUri.startsWith("/static/") &&
                !requestUri.endsWith(".html")) {
            response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
            response.getWriter().write("This service is currently unavailable.");
        }
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
}
