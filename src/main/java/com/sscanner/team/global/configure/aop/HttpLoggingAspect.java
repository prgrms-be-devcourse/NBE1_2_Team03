package com.sscanner.team.global.configure.aop;

import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.global.exception.DuplicateException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.UUID;

@Component
@Aspect
@Slf4j
public class HttpLoggingAspect {


    // Controller의 모든 메서드에 대해 적용
    @Pointcut("execution(* com.sscanner.team..controller.*.*(..))")
    public void pointCut() {}

    @Before("pointCut()")
    public void logHttpRequest(JoinPoint joinPoint) {
        MDC.put("traceId", UUID.randomUUID().toString()); // 멀티 스레드 환경에서도 로그를 구분할 수 있게 해줌
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        log.info("HTTP Request: {} {} from IP {}", request.getMethod(), request.getRequestURI(), request.getRemoteAddr());
    }

    // 메서드 호출 후 정상적으로 반환된 경우 로그 남기기
    @AfterReturning(pointcut = "pointCut()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("Exiting method: {} with result {}", joinPoint.getSignature(), result);
    }

    // 예외 발생 시 로그 남기기
    @AfterThrowing(pointcut = "pointCut()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        if(ex instanceof BadRequestException || ex instanceof DuplicateException || ex instanceof MethodArgumentNotValidException) return;
        log.error("Exception in method: {} with arguments {}. Exception: {}", joinPoint.getSignature(), Arrays.toString(joinPoint.getArgs()), ex.getMessage(), ex);
    }

    @After("pointCut()")
    public void MDCClear() {
        MDC.clear();
    }
}
