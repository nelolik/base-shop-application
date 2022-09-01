package com.nelolik.base_shop.api_gateway.logging;


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
public class ApiRequestLogging {

    @Autowired
    HttpServletRequest request;

    @Pointcut("execution(* com.nelolik.base_shop.api_gateway.controller.*Controller.*(..))")
    public void controllerPointcut() {}

    @Before("controllerPointcut()")
    public void doBeforeRequestHandle(JoinPoint joinPoint) {
        log.info(" RequestURI={},Method={},ParameterMap={}", request.getRequestURI(), request.getMethod(),
                Arrays.toString(joinPoint.getArgs()));
    }

}
