package com.sscanner.team.global.configure.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class TimeTraceAspect {

    @Around("@annotation(com.sscanner.team.global.configure.aop.TimeTrace)")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;

        log.info("{} 실행시간은~~~~~~~ {}ms", joinPoint.getSignature(), executionTime);
        return proceed;
    }
}