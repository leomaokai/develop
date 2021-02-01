package com.kai.server.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
    @GetMapping("/employee/basic/hello")
    public String hello2(){
        return "basic";
    }
    @GetMapping("/employee/advanced/hello")
    public String hello3(){
        return "advanced";
    }
}
