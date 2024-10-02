package com.sscanner.team;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/")
    public String hello() {
        return "hello world2";
    }

    @GetMapping("/health")
    public String health() {
        return "ok";
    }
}
