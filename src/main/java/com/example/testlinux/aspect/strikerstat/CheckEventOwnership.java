package com.example.testlinux.aspect.strikerstat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Маркер «проверять владение соревнованием» для ВСЕГО класса.
 *
 * Вешается на тип (контроллер) — {@link EventOwnershipAspect} перехватит все его методы
 * через pointcut @within и для каждого попытается найти eventId независимо от сигнатуры.
 *
 * Методы, в которых eventId не найден, проверку молча пропускают (см. аспект).
 *
 * Альтернатива самописному @PreAuthorize-выражению: одно объявление на класс вместо
 * повторения SpEL в каждом методе с подгонкой под #eventId / #dto.eventId.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckEventOwnership {
}