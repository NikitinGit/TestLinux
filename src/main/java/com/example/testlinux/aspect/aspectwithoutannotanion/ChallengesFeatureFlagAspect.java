package com.example.testlinux.aspect.aspectwithoutannotanion;

import com.example.testlinux.service.EventServiceTest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ChallengesFeatureFlagAspect {

    private final EventServiceTest eventServiceTest;

    @Before("execution(* com.example.testlinux.controller.AspectController.*(..)) ")
    public void check() {
        eventServiceTest.testAspect();
    }
}
