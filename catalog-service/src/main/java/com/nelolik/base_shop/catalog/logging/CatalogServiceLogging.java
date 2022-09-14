package com.nelolik.base_shop.catalog.logging;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class CatalogServiceLogging {

    private final HttpServletRequest httpRequest;

    @Pointcut("execution(* com.nelolik.base_shop.catalog.controller.*Controller.*(..))")
    public void controllerPointcut() {}

    @Before("controllerPointcut()")
    public void doBeforeRequestHandle(JoinPoint joinPoint) {
        log.info(" RequestURI={},Method={},ParameterMap={}", httpRequest.getRequestURI(), httpRequest.getMethod(),
                Arrays.toString(joinPoint.getArgs()));
    }

}
