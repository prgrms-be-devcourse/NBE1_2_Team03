package com.sscanner.team.points.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/redis")
public class RedisController {
    private static final Logger logger = LoggerFactory.getLogger(RedisController.class);

    /**
     * REDIS 정상 작동 테스트 API
     * @param id 사용자 ID
     * @return 사용자 ID
     */
    @GetMapping()
    @Cacheable(value = "user")
    public String get(@RequestParam(value = "id") String id) {
        logger.info("GET userId:{}", id);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return id;
    }
}