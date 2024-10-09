package com.sscanner.team.test.service;

import org.springframework.stereotype.Service;

@Service
public class TestService {

    public void testLogic() {
        System.out.println("TestService.testLogic");
    }

    public void errorLogic() throws Exception {
        throw new Exception("예외");
    }
}
