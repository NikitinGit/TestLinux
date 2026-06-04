package com.example.testlinux.aspect.acpectonclass;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class InterceptClass {

    @Before("@within(checkAuthorization)")
    public void checkClass(JoinPoint joinPoint, CheckAuthorization checkAuthorization) {
        log.info("checkAuthorization joinPoint.getClass().getName() {}", joinPoint.getClass().getName());
        log.info("class: {}, method: {}",
                joinPoint.getSignature().getDeclaringTypeName(),   // имя класса перехваченного метода
                joinPoint.getSignature().getName());               // имя метода

    }
}
