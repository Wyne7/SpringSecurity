package com.coder.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/home")
public class LoginContoller {


    @GetMapping
    public ResponseEntity<String> login(){
        return ResponseEntity.ok("Login Successful");
    }
    
}
