package com.example.testlinux.aspect.spel;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.expression.spel.standard.SpelExpressionParser;

@Slf4j
@Aspect
@Component("spelOrganizerAccessAspect")
public class OrganizerAccessAspect {

    private final SpelExpressionParser parser = new SpelExpressionParser();

    @Before("@annotation(checkOrganizerAccess)")
    public void check(JoinPoint jp, CheckOrganizerAccess checkOrganizerAccess) {
        MethodSignature sig = (MethodSignature) jp.getSignature();
        StandardEvaluationContext ctx = new StandardEvaluationContext();
        String[] names = sig.getParameterNames();
        Object[] args = jp.getArgs();
        for (int i = 0; i < names.length; i++) {
            ctx.setVariable(names[i], args[i]);
        }
        Integer eventId = parser.parseExpression(checkOrganizerAccess.value()).getValue(ctx, Integer.class);
        log.info("SPEL eventId: {}", eventId);
    }
}
