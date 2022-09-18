package com.nelolik.base_shop.productservice.logging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class ProductServiceLogging {

    private final HttpServletRequest request;

    @Pointcut("execution(* com.nelolik.base_shop.productservice.controller.*Controller.*(..))")
    public void controllerPointcut() {}

    @Before("controllerPointcut()")
    public void doBeforeRequestHandle(JoinPoint joinPoint) {
        log.info(" RequestURI={},Method={},ParameterMap={}", request.getRequestURI(), request.getMethod(),
                Arrays.toString(joinPoint.getArgs()));
    }

}