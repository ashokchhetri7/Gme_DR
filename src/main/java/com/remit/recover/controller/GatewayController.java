package com.remit.recover.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class GatewayController {

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/v1/mobile/Zero/ping")
    public ResponseEntity<String> redirectPing() {
        String targetUrl = "https://gmeuat.gmeremit.com:5022/api/v1/mobile/Zero/ping";
        return restTemplate.getForEntity(targetUrl, String.class);
    }

    @PostMapping("/v1/mobile/getChannelTalk/{customerId}")
    public ResponseEntity<String> redirectGetChannelTalk(@PathVariable String customerId, @RequestBody String body) {
        String targetUrl = "https://gmeuat.gmeremit.com:5022/api/v1/mobile/getChannelTalk/" + customerId;
        return restTemplate.postForEntity(targetUrl, body, String.class);
    }

    @GetMapping("/v1/mobile/GetCustomerServiceContactDetails/remittance")
    public ResponseEntity<String> redirectCustomerServiceContactDetails(@RequestParam String customerld) {
        String targetUrl = "https://gmeuat.gmeremit.com:5022/api/v1/mobile/GetCustomerServiceContactDetails/remittance?customerld=" + customerld;
        return restTemplate.getForEntity(targetUrl, String.class);
    }

    @PostMapping("/v1/mobile/Card/GetCardList")
    public ResponseEntity<String> redirectGetCardList(@RequestBody String body) {
        String targetUrl = "https://gmeuat.gmeremit.com:5022/api/v1/mobile/Card/GetCardList";
        return restTemplate.postForEntity(targetUrl, body, String.class);
    }

    @GetMapping("/v3/Mobile/Information/{customerId}")
    public ResponseEntity<String> redirectMobileInformation(@PathVariable String customerId) {
        String targetUrl = "https://gmeuat.gmeremit.com:5022/api/v3/Mobile/Information/" + customerId;
        return restTemplate.getForEntity(targetUrl, String.class);
    }

    @PostMapping("/v3/mobile/CustomerProfile")
    public ResponseEntity<String> redirectCustomerProfile(@RequestBody String body) {
        String targetUrl = "https://gmeuat.gmeremit.com:5022/api/v3/mobile/CustomerProfile";
        return restTemplate.postForEntity(targetUrl, body, String.class);
    }

    @GetMapping("/v1/mobile/countriesServices")
    public ResponseEntity<String> redirectCountriesServices() {
        String targetUrl = "https://gmeuat.gmeremit.com:5022/api/v1/mobile/countriesServices";
        return restTemplate.getForEntity(targetUrl, String.class);
    }

    @PostMapping("/v2/kftc/GetAdminVerification")
    public ResponseEntity<String> redirectAdminVerification(@RequestBody String body) {
        String targetUrl = "https://gmeuat.gmeremit.com:5022/api/v2/kftc/GetAdminVerification";
        return restTemplate.postForEntity(targetUrl, body, String.class);
    }

    @GetMapping("/v1/mobile/Card/GetBanner/{customerId}")
    public ResponseEntity<String> redirectGetBanner(@PathVariable String customerId) {
        String targetUrl = "https://gmeuat.gmeremit.com:5022/api/v1/mobile/Card/GetBanner/" + customerId;
        return restTemplate.getForEntity(targetUrl, String.class);
    }

    @GetMapping("/v1/coupon/inAppBannerList")
    public ResponseEntity<String> redirectInAppBannerList(@RequestParam String customerld, @RequestParam String launchURL) {
        String targetUrl = "https://gmeuat.gmeremit.com:5022/api/v1/coupon/inAppBannerList/?customerld=" + customerld + "&launchURL=" + launchURL;
        return restTemplate.getForEntity(targetUrl, String.class);
    }

    @PostMapping("/v1/users/access-code")
    public ResponseEntity<String> redirectCustomerAccess(@RequestBody String body) {
        String targetUrl = "https://gmeuat.gmeremit.com:5022/api/v1/users/access-code";
        return restTemplate.postForEntity(targetUrl, body, String.class);
    }
}
