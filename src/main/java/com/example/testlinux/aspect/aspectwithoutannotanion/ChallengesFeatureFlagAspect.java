package com.example.testlinux.aspect.aspectwithoutannotanion;

import com.example.testlinux.service.EventServiceTest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class ChallengesFeatureFlagAspect {

    private final EventServiceTest eventServiceTest;

/*    @Before("execution(* com.example.testlinux.controller.AspectController.*(..)) ")
    public void check(JoinPoint joinPoint) {
        log.info("ChallengesFeatureFlagAspect check(); ");
        Object[] args = joinPoint.getArgs();
        for (var a : args) {
            System.out.println("a.name; " + a.toString());
        }
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getName();                        // имя метода
        String[] paramNames = signature.getParameterNames();            // имена параметров
        Class<?>[] paramTypes = signature.getParameterTypes();          // типы
        Object target = joinPoint.getTarget();                          // целевой объект (AspectController)
        log.info("Перехвачен {} с аргументами {}", methodName, Arrays.toString(args));
        Object arg = args[args.length - 1];
        Integer eventId = arg instanceof Integer ? (Integer) arg : null;eventServiceTest.testAspect(eventId);
    }*/

    @Before("execution(* com.example.testlinux.controller.AspectController.*(..))")
    public void check(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = signature.getParameterNames();   // требует -parameters (Spring Boot включает по умолчанию)
        Object[] args = joinPoint.getArgs();

        log.info("Перехвачен {} с аргументами {}", signature.getName(), Arrays.toString(args));

        Integer eventId = null;
        for (int i = 0; i < paramNames.length; i++) {
            if ("eventId".equals(paramNames[i]) && args[i] instanceof Integer e) {
                eventId = e;
                break;
            }
        }

        eventServiceTest.testAspect(eventId);   // придёт null для endPointN2/N3 — это ок
    }

}
