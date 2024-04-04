package com.example.springsecuritystudy.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/spring-security-study")
public class HelloController {
    @GetMapping("/get")
    public String hello(){
        return "Hello";
    }
}
