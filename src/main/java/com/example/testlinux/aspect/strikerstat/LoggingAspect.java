package com.example.testlinux.aspect.strikerstat;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(public * com.example.testlinux..*(..))")
    public void appPublicMethods() {}

    @Pointcut("within(org.springframework.web.filter.GenericFilterBean+) || target(jakarta.servlet.Filter)")
    public void servletFilters() {}

    @Pointcut("appPublicMethods() && !servletFilters()")
    public void appMethodsExceptFilters() {}

    @PostConstruct
    public void init() {
        log.info("LoggingAspect initialized");
    }

    @Around("appMethodsExceptFilters()")
    public Object logAround(ProceedingJoinPoint pjp) throws Throwable {
        if (log.isDebugEnabled()) {
            log.debug("→ {}.{} args={}",
                    pjp.getSignature().getDeclaringTypeName(),
                    pjp.getSignature().getName(),
                    Arrays.toString(pjp.getArgs()));
        }
        try {
            Object result = pjp.proceed();
            if (log.isDebugEnabled()) {
                log.debug("← {}.{} -> {}",
                        pjp.getSignature().getDeclaringTypeName(),
                        pjp.getSignature().getName(),
                        result);
            }
            return result;
        } catch (Throwable ex) {
            log.error("✖ {}.{} args={} -> {}",
                    pjp.getSignature().getDeclaringTypeName(),
                    pjp.getSignature().getName(),
                    Arrays.toString(pjp.getArgs()),
                    ex.getMessage(), ex);
            throw ex;
        }
    }
}