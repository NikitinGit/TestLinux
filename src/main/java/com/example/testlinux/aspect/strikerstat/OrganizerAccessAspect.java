package com.example.testlinux.aspect.strikerstat;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class OrganizerAccessAspect {

    @Autowired
    private AccessService accessService;

 /*   @Before("@annotation(checkOrganizerAccess)")
    public void checkOrganizerAccess(JoinPoint joinPoint, CheckOrganizerAccess checkOrganizerAccess) {
        Integer eventId = null;

        if (checkOrganizerAccess.eventIdParamIndex() >= 0) {
            // Direct argument (eg. int eventId)
            Object arg = joinPoint.getArgs()[checkOrganizerAccess.eventIdParamIndex()];
            if (arg instanceof Integer) {
                eventId = (Integer) arg;
            }
        }

        if (eventId == null && !checkOrganizerAccess.eventIdFieldName().isEmpty()) {
            // Objekt with eventId as field
            for (Object arg : joinPoint.getArgs()) {
                eventId = extractEventIdFromObject(arg, checkOrganizerAccess.eventIdFieldName());
                if (eventId != null) break;
            }
        }

        if (eventId == null) {
            String endpoint = "" + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
            String params = Arrays.stream(joinPoint.getArgs()).toList().toString();
            throw new IllegalArgumentException("Event-ID could not be extracted from parameters: " + params +
                    " for endpoint " + endpoint + " with aspect-parameters: " + checkOrganizerAccess.eventIdParamIndex() +
                    " and " + checkOrganizerAccess.eventIdFieldName());
        }

        Integer organizerLoginId = 4; //getLoginId();

        if (!accessService.doesEventBelongToOrganizer(organizerLoginId, eventId)) {
            log.warn("Соревнование {} не принадлежит организатору {}", eventId, organizerLoginId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "У вас нет доступа к этому соревнованию");
        }
    }

    private Integer extractEventIdFromObject(Object obj, String fieldName) {
        if (obj == null) return null;
        try {
            Field field = getFieldRecursively(obj.getClass(), fieldName);
            if (field != null) {
                field.setAccessible(true);
                Object value = field.get(obj);
                if (value instanceof Integer) {
                    return (Integer) value;
                }
            }
        } catch (IllegalAccessException e) {
            log.error("Error when accessing field {} from class {}: {}", fieldName, obj.getClass().getName(), e.getMessage());
        }
        return null;
    }

    private Field getFieldRecursively(Class<?> clazz, String fieldName) {
        while (clazz != null) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }

    protected Auth getAuth(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) return null;
        try {
            return (Auth) authentication.getPrincipal();
        } catch (ClassCastException e){
            return new Auth(null, null, false, null);
        }
    }

    protected Integer getLoginId(){
        return getAuth().getLoginId();
    }*/

    @Before("@annotation(checkOrganizerAccess)")
    public void check2(JoinPoint joinPoint, CheckOrganizerAccess checkOrganizerAccess) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        Integer eventId = null;
        for (int i = 0; i < paramNames.length; i++) {
            if ("eventId".equals(paramNames[i]) && args[i] instanceof Integer e) {
                eventId = e;
                break;
            }
        }

        log.info("eventId; {}", eventId);
    }
}