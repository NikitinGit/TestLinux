package com.example.testlinux.controller;

import com.example.testlinux.aspect.acpectonclass.CheckAuthorization;
import com.example.testlinux.dto.AuthorizeEventDto;
import com.example.testlinux.service.EventServiceTest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
@CheckAuthorization
public class EventController {

    private final EventServiceTest eventServiceTest;

    /**
     * Пример Spring-native method-security (аналог самописного @CheckOrganizerAccess).
     *
     * @PreAuthorize вычисляется ДО входа в тело метода, через AOP-прокси бина.
     * SpEL-выражение проверяет ДВА условия через and (с короткой схемой вычисления):
     *   1. hasAuthority('Organizer')                         — слой роли (как в URL-правилах)
     *   2. @accessService.doesEventBelongToOrganizer(...)    — слой владения (запрос в БД)
     *
     * Доступные в SpEL переменные:
     *   principal       — это authentication.getPrincipal(), у нас = объект Auth
     *                     (его кладёт TokenAuthenticationFilter как 2-й аргумент
     *                      RememberMeAuthenticationToken). principal.loginId -> Auth.getLoginId()
     *   #eventId        — аргумент метода по имени (требует -parameters при компиляции
     *                     или сработает по позиции)
     *   @accessService  — бин по имени из контекста Spring (класс AccessService, @Service)
     *
     * Важно: and в SpEL короткозамкнут — если hasAuthority вернул false (например,
     * анонимный запрос), второе условие НЕ вычисляется, обращения к БД и principal.loginId
     * не будет, NPE не возникнет. При провале любого условия -> 403 (AccessDeniedException).
     */
    @PreAuthorize("hasAuthority('Organizer') and @accessService.doesEventBelongToOrganizer(principal.loginId, #eventId)")
    @PutMapping("/update")
    public ResponseEntity<Void> updateEvent(@RequestParam("eventId") Integer eventId) {
        // Сюда управление дойдёт ТОЛЬКО если оба условия @PreAuthorize прошли:
        log.info("updateEvent разрешён: eventId={}", eventId);
        eventServiceTest.setEventId(eventId);
        return ResponseEntity.ok().build();
    }

    /**
     * eventId лежит ВНУТРИ DTO, а не отдельным аргументом.
     *
     * Поэтому #eventId здесь НЕ сработал бы — такой переменной-аргумента нет
     * (аргумент называется dto). Достаём поле навигацией по свойству:
     *   #dto.eventId  ->  dto.getEventId()
     *
     * SpEL умеет ходить внутрь объектов через точку: #имяАргумента.поле -> геттер.
     *
     * Путь намеренно отличается от /update — два метода на одном @PutMapping("/update")
     * дают Ambiguous mapping и приложение не стартует.
     */
    @PreAuthorize("hasAuthority('Organizer') and @accessService.doesEventBelongToOrganizer(principal.loginId, #dto.eventId)")
    @PutMapping("/update-from-dto")
    public ResponseEntity<Void> setEventFromDto(@RequestBody AuthorizeEventDto dto) {
        log.info("setEventFromDto разрешён: dto.getEventId()={}", dto.getEventId());
        eventServiceTest.setEventId(dto.getEventId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/method")
    public ResponseEntity<Void> method() {
        //eventServiceTest.getData(175);
        eventServiceTest.demonstrateEqualsHashCodeProblem();
        //eventServiceTest.demonstrateLazyAssociationProxyProblem();
        //eventServiceTest.demonstrateGetReferenceProblem();
        return ResponseEntity.ok().build();
    }

    //@CheckOrganizerAccess(eventIdFieldName = "eventId") - не работает потому что не в дто
    //@CheckOrganizerAccess(eventIdParamIndex = 0)
    @RequestMapping(value = "/aspect", method = RequestMethod.GET)
    public ResponseEntity<Void> aspect(@RequestParam("eventId") Integer eventId) {
        eventServiceTest.setEventId(eventId);
        return ResponseEntity.ok().build();
    }

    //@CheckOrganizerAccess
    @RequestMapping(value = "/aspect2", method = RequestMethod.GET)
    public ResponseEntity<Void> aspect2(@RequestParam("eventId") Integer eventId) {
        eventServiceTest.setEventId(eventId * 2);
        return ResponseEntity.ok().build();
    }
}
