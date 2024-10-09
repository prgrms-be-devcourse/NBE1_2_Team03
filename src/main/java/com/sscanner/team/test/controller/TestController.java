package com.sscanner.team.test.controller;

import com.sscanner.team.global.configure.aop.TimeTrace;
import com.sscanner.team.test.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/test")
    public String test() {
        testService.testLogic();
        return "test";
    }

    @GetMapping("/err_test")
    public String error_test() throws Exception {
        testService.errorLogic();
        return "err";
    }
}
