package com.nelolik.base_shop.statistic_service.logging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Controller
@Slf4j
@RequiredArgsConstructor
public class StatisticServiceHttpRequestLogging {
    private final HttpServletRequest request;

    @Pointcut("execution(* com.nelolik.base_shop.statistic_service.controller.*Controller.*(..))")
    public void controllerPointcut() {}

    @Before("controllerPointcut()")
    public void doBeforeRequestHandle(JoinPoint joinPoint) {
        log.info(" RequestURI={},Method={},ParameterMap={}", request.getRequestURI(), request.getMethod(),
                Arrays.toString(joinPoint.getArgs()));
    }

}
