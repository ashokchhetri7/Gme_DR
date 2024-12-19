package com.remit.recover;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @GetMapping("/favicon.ico")
    public ResponseEntity<Void> handleFaicon(){
        //
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}