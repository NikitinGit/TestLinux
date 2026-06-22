package com.example.testlinux.aspect.strikerstat;

import com.example.testlinux.security.conf.Auth;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;

/**
 * «Умный» аспект проверки владения соревнованием.
 *
 * Вешается на КЛАСС через {@link CheckEventOwnership} (pointcut @within) и срабатывает
 * перед КАЖДЫМ методом такого класса. eventId извлекается независимо от сигнатуры:
 *   1) прямой аргумент с именем eventId (int/Integer);
 *   2) поле eventId внутри любого объекта-аргумента (DTO), рекурсивно по иерархии классов.
 *
 * Это то, чего НЕ может статический @PreAuthorize: SpEL привязан к именам параметров
 * (#eventId / #dto.eventId) и одно выражение на класс не подстроится под разные сигнатуры.
 * Здесь же — императивный Java-код, который видит joinPoint.getArgs() и копает рефлексией.
 *
 * Политика для краёв:
 *   - eventId не найден  -> метод не «про соревнование», проверка ПРОПУСКАЕТСЯ (не падаем);
 *   - нет аутентификации -> 401;
 *   - соревнование чужое -> 403.
 *
 * Роль (Organizer) тут отдельно не проверяется: doesEventBelongToOrganizer сверяет
 * events.organizer_login = loginId, поэтому id не-организатора просто не совпадёт ни с одним
 * соревнованием -> 403. Если нужна явная проверка роли — добавь на класс
 * @PreAuthorize("hasAuthority('Organizer')").
 */
@Slf4j
@Aspect
@Component
public class EventOwnershipAspect {

    @Autowired
    private AccessService accessService;

    @Before("@within(checkEventOwnership)")
    public void check(JoinPoint joinPoint, CheckEventOwnership checkEventOwnership) {
        String where = joinPoint.getSignature().getDeclaringType().getSimpleName()
                + "." + joinPoint.getSignature().getName();

        Integer eventId = extractEventId(joinPoint);
        if (eventId == null) {
            // Метод без eventId (например, листинг или health) — не наша забота.
            log.debug("EventOwnership: eventId не найден в {} — проверка пропущена", where);
            return;
        }

        Integer loginId = currentLoginId();
        if (loginId == null) {
            log.warn("EventOwnership: нет аутентифицированного пользователя в {}", where);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Не аутентифицирован");
        }

        if (!accessService.doesEventBelongToOrganizer(loginId, eventId)) {
            log.warn("EventOwnership: соревнование {} не принадлежит организатору {} ({})", eventId, loginId, where);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "У вас нет доступа к этому соревнованию");
        }

        log.debug("EventOwnership: доступ разрешён — организатор {} владеет соревнованием {} ({})", loginId, eventId, where);
    }

    /**
     * Ищет eventId независимо от сигнатуры: сперва как прямой аргумент по имени,
     * затем как поле eventId внутри объектов-аргументов.
     */
    private Integer extractEventId(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        // 1) Прямой аргумент с именем eventId (int автобоксится в Integer).
        if (paramNames != null) {
            for (int i = 0; i < paramNames.length; i++) {
                if ("eventId".equals(paramNames[i]) && args[i] instanceof Integer e) {
                    return e;
                }
            }
        }

        // 2) Поле eventId внутри любого объекта-аргумента (DTO и т.п.).
        for (Object arg : args) {
            if (arg == null) continue;
            if (isJdkType(arg)) continue; // не лезем рефлексией в String/Number/коллекции JDK
            Integer fromField = extractEventIdFromObject(arg, "eventId");
            if (fromField != null) {
                return fromField;
            }
        }

        return null;
    }

    /** Текущий пользователь из SecurityContext (principal кладёт TokenAuthenticationFilter как Auth). */
    private Integer currentLoginId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        if (authentication.getPrincipal() instanceof Auth auth) {
            return auth.getLoginId();
        }
        return null;
    }

    private boolean isJdkType(Object obj) {
        Package pkg = obj.getClass().getPackage();
        return pkg != null && pkg.getName().startsWith("java.");
    }

    private Integer extractEventIdFromObject(Object obj, String fieldName) {
        Field field = getFieldRecursively(obj.getClass(), fieldName);
        if (field == null) {
            return null;
        }
        try {
            field.setAccessible(true);
            Object value = field.get(obj);
            if (value instanceof Integer i) {
                return i;
            }
        } catch (IllegalAccessException e) {
            log.error("EventOwnership: не удалось прочитать поле {} из {}: {}",
                    fieldName, obj.getClass().getName(), e.getMessage());
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
}