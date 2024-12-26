package com.remit.recover.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import jakarta.servlet.http.HttpServletRequest;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class GatewayController {
    private final Logger logger = LoggerFactory.getLogger(GatewayController.class);

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${target.base.url}")
    private String targetBaseUrl;

    @GetMapping("/v1/mobile/Zero/ping")
    public ResponseEntity<String> redirectPing() {
        String targetUrl = targetBaseUrl + "/api/v1/mobile/Zero/ping";
        ResponseEntity<String> response = restTemplate.getForEntity(targetUrl, String.class);
        logger.info("Received response for URL {}: Status {}, Body: {}", targetUrl, response.getStatusCode(), response.getBody());
        return response;
    }

    @PostMapping("/v1/mobile/getChannelTalk/{id}")
    public ResponseEntity<String> forwardGetChannelTalk(
            @PathVariable("id") String id,
            @RequestBody(required = false) String body,
            HttpServletRequest request) {

        // Construct the final target URL
        String targetUrl = targetBaseUrl + "/api/v1/mobile/getChannelTalk/" + id;

        // Dynamically extract all headers from the incoming request
        HttpHeaders headers = new HttpHeaders();
        Collections.list(request.getHeaderNames()).forEach(headerName -> {
            // Use add() rather than set() if you want to preserve potential multi-value headers
            headers.add(headerName, request.getHeader(headerName));
        });

        // Optionally, set a default Content-Type if it isn't present
        if (!headers.containsKey(HttpHeaders.CONTENT_TYPE)) {
            headers.setContentType(MediaType.APPLICATION_JSON);
        }

        // Create the request entity with the incoming body and headers
        HttpEntity<String> httpEntity = new HttpEntity<>(body, headers);

        // Forward the request using RestTemplate
        ResponseEntity<String> response = restTemplate.postForEntity(targetUrl, httpEntity, String.class);

        // Log the response for debugging
        logger.info("Forwarded POST to {} => Response Status: {}, Body: {}",
                targetUrl, response.getStatusCode(), response.getBody());

        // Return the response as-is
        return response;
    }


    @PostMapping("/v1/mobile/GetCustomerServiceContactDetails/{anyPath}")
    public ResponseEntity<String> forwardGetCustomerServiceContactDetails(
            @PathVariable String anyPath,
            @RequestParam Map<String, String> queryParams,
            @RequestBody(required = false) String body,
            HttpServletRequest request
    ) {

        StringBuilder urlBuilder = new StringBuilder(targetBaseUrl)
                .append("/api/v1/mobile/GetCustomerServiceContactDetails/")
                .append(anyPath);

        if (!queryParams.isEmpty()) {
            // Append query parameters, properly URL-encoded.
            String queryString = queryParams.entrySet().stream()
                    .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                    .collect(Collectors.joining("&"));
            urlBuilder.append('?').append(queryString);
        }

        String targetUrl = urlBuilder.toString();

        // 2) Copy all incoming headers to the outbound request.
        HttpHeaders headers = new HttpHeaders();
        Collections.list(request.getHeaderNames()).forEach(headerName -> {
            // Some headers can appear multiple times (e.g., "Cookie")
            // so we add each value individually.
            List<String> values = Collections.list(request.getHeaders(headerName));
            for (String value : values) {
                headers.add(headerName, value);
            }
        });

        // 3) Build the request entity with body + headers.
        //    Note that it's unconventional to have a body in a GET request,
        //    but this replicates the original Unirest behavior.
        HttpEntity<String> httpEntity = new HttpEntity<>(body, headers);

        // 4) Forward as a GET request using RestTemplate.
        ResponseEntity<String> externalResponse = restTemplate.exchange(
                targetUrl,
                HttpMethod.GET,
                httpEntity,
                String.class
        );

        // 5) Return the external API’s response exactly as we got it:
        //    same status code, same body, etc.
        return ResponseEntity
                .status(externalResponse.getStatusCode())
                .headers(externalResponse.getHeaders())
                .body(externalResponse.getBody());
    }

    @PostMapping("/v1/mobile/Card/GetCardList")
    public ResponseEntity<String> forwardCardGetList(
            @RequestBody(required = false) String body,  // Capture JSON or any body
            HttpServletRequest request
    ) {

        String targetUrl = targetBaseUrl + "/api/v1/mobile/Card/GetCardList";

        // 2) Copy all incoming headers into a new HttpHeaders object
        HttpHeaders headers = new HttpHeaders();
        Collections.list(request.getHeaderNames()).forEach(headerName -> {
            List<String> values = Collections.list(request.getHeaders(headerName));
            for (String value : values) {
                headers.add(headerName, value);
            }
        });

        // (Optionally) Ensure we set the content type if needed
        // We assume JSON, but we do NOT hardcode if you want to pass others
        if (!headers.containsKey(HttpHeaders.CONTENT_TYPE)) {
            headers.setContentType(MediaType.APPLICATION_JSON);
        }

        // 3) Create the request entity with the copied headers and the exact body
        HttpEntity<String> httpEntity = new HttpEntity<>(body, headers);

        // 4) Forward the request to the external service as a POST
        ResponseEntity<String> externalResponse =
                restTemplate.postForEntity(targetUrl, httpEntity, String.class);

        // 5) Return the exact status code, headers, and body from the external API
        return ResponseEntity
                .status(externalResponse.getStatusCode())
                .headers(externalResponse.getHeaders())
                .body(externalResponse.getBody());
    }

    @GetMapping("/v3/Mobile/Information/{customerId}")
    public ResponseEntity<String> redirectMobileInformation(@PathVariable String customerId) {
        String targetUrl =  targetBaseUrl + "/api/v3/Mobile/Information/" + customerId;
        return restTemplate.getForEntity(targetUrl, String.class);
    }

    @PostMapping("/v3/mobile/CustomerProfile")
    public ResponseEntity<String> redirectCustomerProfile(@RequestBody String body, HttpServletRequest request) {
        String targetUrl = targetBaseUrl + "/api/v3/mobile/CustomerProfile";

        HttpHeaders headers = new HttpHeaders();
        Collections.list(request.getHeaderNames()).forEach(headerName -> {
            if (!headerName.equalsIgnoreCase("host") &&
                    !headerName.equalsIgnoreCase("content-length") &&
                    !headerName.equalsIgnoreCase("connection")) {
                headers.add(headerName, request.getHeader(headerName));
            }
        });

        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(body, headers);

        try {
            // Make the request
            ResponseEntity<String> response = restTemplate.postForEntity(targetUrl, httpEntity, String.class);
            // Return 2xx responses as-is
            return response;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // For 4xx/5xx, extract the body manually
            HttpStatus statusCode = (HttpStatus) e.getStatusCode();
            String errorBody = e.getResponseBodyAsString();  // the JSON error from remote server
            // Log or do anything else you need
            logger.error("Remote server returned error: status={}, body={}", statusCode, errorBody);

            // Return the same status and body to the client
            return ResponseEntity.status(statusCode).body(errorBody);
        }
    }

    @GetMapping("/v1/mobile/countriesServices")
    public ResponseEntity<String> forwardGetCountriesServices(
            @RequestParam Map<String, String> queryParams,
            @RequestBody(required = false) String body,
            HttpServletRequest request
    ) {
        // 1) Build the full target URL (including query parameters if present)
        StringBuilder urlBuilder = new StringBuilder(targetBaseUrl)
                .append("/api/v1/mobile/countriesServices");

        if (!queryParams.isEmpty()) {
            String queryString = queryParams.entrySet().stream()
                    .map(entry -> entry.getKey() + "="
                            + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                    .collect(Collectors.joining("&"));
            urlBuilder.append("?").append(queryString);
        }

        String targetUrl = urlBuilder.toString();

        // 2) Copy all incoming headers to the outbound request
        HttpHeaders headers = new HttpHeaders();
        Collections.list(request.getHeaderNames()).forEach(headerName -> {
            List<String> values = Collections.list(request.getHeaders(headerName));
            for (String value : values) {
                headers.add(headerName, value);
            }
        });


        HttpEntity<String> httpEntity = new HttpEntity<>(body, headers);

        // 4) Make the GET call to the external service
        ResponseEntity<String> externalResponse = restTemplate.exchange(
                targetUrl,
                HttpMethod.GET,
                httpEntity,
                String.class
        );

        // 5) Return the external service's response exactly
        return ResponseEntity
                .status(externalResponse.getStatusCode())
                .headers(externalResponse.getHeaders())
                .body(externalResponse.getBody());
    }


    @PostMapping("/v2/kftc/GetAdminVerification")
    public ResponseEntity<String> forwardGetAdminVerification(
            @RequestBody(required = false) String body,   // Capture the request body if present
            HttpServletRequest request
    ) {
        // 1) Construct the target URL
        String targetUrl = targetBaseUrl + "/api/v2/kftc/GetAdminVerification";

        // 2) Copy all incoming headers
        HttpHeaders headers = new HttpHeaders();
        Collections.list(request.getHeaderNames()).forEach(headerName -> {
            List<String> values = Collections.list(request.getHeaders(headerName));
            for (String value : values) {
                headers.add(headerName, value);
            }
        });

        // 3) Build the request entity with headers + body
        HttpEntity<String> httpEntity = new HttpEntity<>(body, headers);

        // 4) Make the POST request via RestTemplate
        ResponseEntity<String> externalResponse =
                restTemplate.postForEntity(targetUrl, httpEntity, String.class);

        // 5) Return the external service’s status, headers, and body verbatim
        return ResponseEntity
                .status(externalResponse.getStatusCode())
                .headers(externalResponse.getHeaders())
                .body(externalResponse.getBody());
    }

    @GetMapping("/v1/mobile/Card/GetBanner/{id}")
    public ResponseEntity<String> forwardGetBanner(
            @PathVariable String id,
            @RequestParam Map<String, String> queryParams,
            HttpServletRequest request
    ) {
        // 1) Build the target URL, including path variable and any query parameters
        StringBuilder urlBuilder = new StringBuilder(targetBaseUrl)
                .append("/api/v1/mobile/Card/GetBanner/")
                .append(id);

        // If query parameters exist (e.g. ?customerId=1234), append them
        if (!queryParams.isEmpty()) {
            String queryString = queryParams.entrySet().stream()
                    .map(entry -> entry.getKey() + "="
                            + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                    .collect(Collectors.joining("&"));
            urlBuilder.append("?").append(queryString);
        }

        String targetUrl = urlBuilder.toString();

        // 2) Copy all incoming headers from the HttpServletRequest
        HttpHeaders headers = new HttpHeaders();
        Collections.list(request.getHeaderNames()).forEach(headerName -> {
            List<String> values = Collections.list(request.getHeaders(headerName));
            for (String value : values) {
                headers.add(headerName, value);
            }
        });

        // 3) Create the HTTP entity (GET typically doesn't include a body)
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);

        // 4) Forward as GET to the external API
        ResponseEntity<String> externalResponse = restTemplate.exchange(
                targetUrl,
                HttpMethod.GET,
                httpEntity,
                String.class
        );

        // 5) Return the external service response verbatim
        return ResponseEntity
                .status(externalResponse.getStatusCode())
                .headers(externalResponse.getHeaders())
                .body(externalResponse.getBody());
    }

    @GetMapping("/v1/coupon/inAppBannerList")
    public ResponseEntity<String> forwardInAppBannerList(
            @RequestParam Map<String, String> queryParams,
            HttpServletRequest request
    ) {
        // 1) Build the target URL (path + query parameters).
        //    e.g. "https://mobileapi.gmeremit.com:8002/api/v1/coupon/inAppBannerList?customerId=377584&launchURL=HOME"
        StringBuilder urlBuilder = new StringBuilder(targetBaseUrl)
                .append("/api/v1/coupon/inAppBannerList");

        if (!queryParams.isEmpty()) {
            String queryString = queryParams.entrySet().stream()
                    .map(entry -> entry.getKey() + "="
                            + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                    .collect(Collectors.joining("&"));
            urlBuilder.append("?").append(queryString);
        }

        String targetUrl = urlBuilder.toString();

        // 2) Copy all incoming headers to the outbound request
        HttpHeaders headers = new HttpHeaders();
        Collections.list(request.getHeaderNames()).forEach(headerName -> {
            List<String> values = Collections.list(request.getHeaders(headerName));
            for (String value : values) {
                headers.add(headerName, value);
            }
        });

        // 3) Create the request entity (no body for a GET)
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);

        // 4) Make the GET request to the external service
        ResponseEntity<String> externalResponse = restTemplate.exchange(
                targetUrl,
                HttpMethod.GET,
                httpEntity,
                String.class
        );

        // 5) Return the external API’s response exactly
        return ResponseEntity
                .status(externalResponse.getStatusCode())
                .headers(externalResponse.getHeaders())
                .body(externalResponse.getBody());
    }

    @PostMapping("/v1/users/access-code")
    public ResponseEntity<String> redirectCustomerAccess(@RequestBody String body, HttpServletRequest request) {
        String targetUrl = targetBaseUrl + "/api/v1/users/access-code";
//        String targetUrl =  targetBaseUrl + "api/v1/users/access-code";
        // Extract headers from the incoming request
        HttpHeaders headers = new HttpHeaders();
        Collections.list(request.getHeaderNames()).forEach(headerName -> {
            headers.set(headerName, request.getHeader(headerName));
        });

        // Ensure Content-Type is set to application/json
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the HTTP request entity
        HttpEntity<String> httpEntity = new HttpEntity<>(body, headers);

        // Make the POST request
        ResponseEntity<String> response = restTemplate.postForEntity(targetUrl, httpEntity, String.class);

        // Log the response
        logger.info("Received response for URL {}: Status {}, Body: {}", targetUrl, response.getStatusCode(), response.getBody());

        // Return the response
        return response;
    }

    @PostMapping("/v1/mobile/ExpiredPassword")
    public ResponseEntity<String> forwardExpiredPasswordRequest(
            @RequestBody(required = false) String body,  // The request body (JSON, etc.)
            HttpServletRequest request
    ) {
        // 1) Construct the target URL
        String targetUrl = targetBaseUrl + "/api/v1/mobile/ExpiredPassword";

        // 2) Copy all incoming headers
        HttpHeaders headers = new HttpHeaders();
        Collections.list(request.getHeaderNames()).forEach(headerName -> {
            List<String> headerValues = Collections.list(request.getHeaders(headerName));
            for (String value : headerValues) {
                headers.add(headerName, value);
            }
        });

        // 3) Build the request entity
        HttpEntity<String> httpEntity = new HttpEntity<>(body, headers);

        // 4) Forward the request to the external API
        ResponseEntity<String> externalResponse =
                restTemplate.postForEntity(targetUrl, httpEntity, String.class);

        // 5) Return the external API's response verbatim
        return ResponseEntity
                .status(externalResponse.getStatusCode())
                .headers(externalResponse.getHeaders())
                .body(externalResponse.getBody());
    }

    @PostMapping("/v1/mobile/ConfirmPassword")
    public ResponseEntity<String> redirectConfirmPassword(@RequestBody String body, HttpServletRequest request) {
        String targetUrl = targetBaseUrl + "/api/v1/mobile/ConfirmPassword";

        // Extract headers dynamically from the incoming request
        HttpHeaders headers = new HttpHeaders();
        Collections.list(request.getHeaderNames()).forEach(headerName -> {
            String headerValue = request.getHeader(headerName);
            headers.set(headerName, headerValue);
        });

        // Ensure Content-Type is set to application/json
        if (!headers.containsKey(HttpHeaders.CONTENT_TYPE)) {
            headers.setContentType(MediaType.APPLICATION_JSON);
        }

        HttpEntity<String> httpEntity = new HttpEntity<>(body, headers);

        try {
            // Make the POST request to the target URL
            ResponseEntity<String> response = restTemplate.postForEntity(targetUrl, httpEntity, String.class);
            return response; // Forward the response as it is
        } catch (HttpClientErrorException e) {
            // Extract the response body and status from the exception
            String errorResponse = e.getResponseBodyAsString();
            HttpStatus statusCode = (HttpStatus) e.getStatusCode();

            // Log the error for debugging
            logger.error("Error during API call to {}: {} - {}", targetUrl, statusCode, errorResponse);

            // Return the response body and status code as received from the downstream API
            return ResponseEntity.status(statusCode).body(errorResponse);
        }
    }

}
