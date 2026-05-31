package com.example.testlinux.service;

import com.example.testlinux.domain.Event;
import com.example.testlinux.domain.EventBidFighter;
import com.example.testlinux.repository.EventRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceTest {

    private final EventRepository eventRepository;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Демонстрирует проблему, которая возникает БЕЗ @EqualsAndHashCode и БЕЗ ручной реализации.
     * Сейчас Event использует equals/hashCode от Object (по ссылке).
     *
     * Сценарий: одна и та же запись в БД, загруженная два раза, даёт два разных Java-объекта.
     * Object.equals сравнивает по ссылке → false. HashSet/HashMap "теряют" сущность,
     * хотя по бизнес-смыслу это один и тот же event.
     *
     * Если раскомментировать ручной equals/hashCode по id в Event — все три ПРОБЛЕМЫ
     * ниже превратятся в true / найденное значение.
     */
    @Transactional
    public void demonstrateEqualsHashCodeProblem() {
        // 1) Создаём и реально сохраняем 2 события в БД
        Event a = new Event();
        a.setNameEvent("Турнир А");
        a.setRingsCount(2);
        Event savedA = eventRepository.save(a);

        Event b = new Event();
        b.setNameEvent("Турнир Б");
        b.setRingsCount(2);
        Event savedB = eventRepository.save(b);

        log.info("savedA.id={}, savedB.id={}", savedA.getEventId(), savedB.getEventId());

        // 2) Кладём сохранённые сущности в HashSet — здесь всё ок, 2 разных объекта = 2 элемента
        Set<Event> set = new HashSet<>();
        set.add(savedA);
        set.add(savedB);
        log.info("Размер HashSet после save: {} (корректно 2)", set.size());

        // 3) Сбрасываем persistence context — следующий find пойдёт в БД
        //    и вернёт НОВЫЙ Java-объект, а не закэшированный managed-экземпляр.
        entityManager.flush();
        entityManager.clear();

        // 4) Загружаем ту же запись заново. Это другой Java-объект.
        Event reloadedA = eventRepository.findEventByEventId(savedA.getEventId())
                .orElseThrow(RuntimeException::new);

        log.warn("savedA == reloadedA      : {}  (false — ожидаемо, разные ссылки)",
                savedA == reloadedA);

        log.warn("savedA.equals(reloadedA) : {}  (ПРОБЛЕМА: false, хотя это ОДНА И ТА ЖЕ запись в БД)",
                savedA.equals(reloadedA));

        log.warn("set.contains(reloadedA)  : {}  (ПРОБЛЕМА: false, HashSet не узнаёт сущность из БД)",
                set.contains(reloadedA));

        // 5) То же самое в HashMap — типичный сценарий "положили по сущности → потом не найти"
        Map<Event, String> map = new HashMap<>();
        map.put(savedA, "значение A");
        log.warn("map.get(reloadedA)       : {}  (ПРОБЛЕМА: null, хотя ключ — та же сущность)",
                map.get(reloadedA));
    }

    public void getData(int eventId) {
        Event event = eventRepository.findEventByEventId(eventId)
                .orElseThrow(RuntimeException::new);
        List<EventBidFighter> bids = event.getEventBidFighters();
        for (EventBidFighter bid : bids) {
            log.info("bid.getFighter().getFirstName(): {}", bid.getFighter().getFirstName());
        }
    }

}
