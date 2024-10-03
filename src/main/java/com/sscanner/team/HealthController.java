package com.sscanner.team;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    @GetMapping("/")
    public String hello() {
        return "hello world";
    }

    @GetMapping("/health")
    public String health() {
        return "ok";
    }
}
