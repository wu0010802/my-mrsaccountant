package com.example.mrsaccountant.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mrsaccountant")
public class test {

    @GetMapping("test")
    public ResponseEntity<?> testConnect() {
        return ResponseEntity.ok("success connected");
    }

}
