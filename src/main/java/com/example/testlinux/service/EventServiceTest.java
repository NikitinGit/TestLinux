package com.example.testlinux.service;

import com.example.testlinux.domain.Event;
import com.example.testlinux.domain.EventBidFighter;
import com.example.testlinux.repository.EventBidFighterRepository;
import com.example.testlinux.repository.EventRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

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
    private final EventBidFighterRepository bidRepository;
    private final PlatformTransactionManager transactionManager;

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

    /**
     * Пример 1: разные транзакции.
     * Сохраняем event в одной транзакции, загружаем его в двух последующих.
     * Каждый раз — новая сессия Hibernate, новый persistence context,
     * новый Java-объект. equals по ссылке (Object.equals) их не сравнивает.
     */
    public void demonstrateDifferentTransactionsProblem() {
        TransactionTemplate tx = new TransactionTemplate(transactionManager);

        Integer id = tx.execute(status -> {
            Event e = new Event();
            e.setNameEvent("Турнир межтранзакционный");
            e.setRingsCount(2);
            return eventRepository.save(e).getEventId();
        });

        Event loaded1 = tx.execute(status ->
                eventRepository.findEventByEventId(id).orElseThrow(RuntimeException::new));

        Event loaded2 = tx.execute(status ->
                eventRepository.findEventByEventId(id).orElseThrow(RuntimeException::new));

        log.warn("loaded1 == loaded2          : {}  (false — разные транзакции, разные объекты)",
                loaded1 == loaded2);
        log.warn("loaded1.equals(loaded2)     : {}  (ПРОБЛЕМА: false, но это одна и та же запись в БД)",
                loaded1.equals(loaded2));
    }

    /**
     * Пример 2: HibernateProxy.
     * entityManager.getReference(...) возвращает НЕинициализированный прокси —
     * именно так Hibernate отдаёт lazy-связи (например, bid.getEvent()).
     * Класс прокси отличается от Event.class, поэтому "наивный" IDE-generated equals,
     * который начинается с if (getClass() != o.getClass()) return false — всегда вернёт false,
     * хотя по бизнес-смыслу это та же сущность. Именно ради этого случая
     * закомментированный equals в Event.java разворачивает HibernateProxy через
     * getHibernateLazyInitializer().getPersistentClass().
     */
    @Transactional
    public void demonstrateHibernateProxyProblem() {
        Event e = new Event();
        e.setNameEvent("Турнир для прокси");
        e.setRingsCount(2);
        Integer id = eventRepository.save(e).getEventId();
        entityManager.flush();
        entityManager.clear();

        // Не идём в БД — получаем прокси на entity
        Event proxy = entityManager.getReference(Event.class, id);

        log.warn("proxy.getClass()                    : {}", proxy.getClass().getName());
        log.warn("Event.class                         : {}", Event.class.getName());
        log.warn("proxy instanceof HibernateProxy     : {}", proxy instanceof HibernateProxy);
        log.warn("proxy.getClass() == Event.class     : {}  (ПРОБЛЕМА: классы разные — IDE-generated equals провалится на getClass()-чекере)",
                proxy.getClass() == Event.class);
    }

    /**
     * Пример 3: detach.
     * После detach() managed-копия пропадает из persistence context.
     * Следующий find() / findById() идёт в БД и возвращает НОВЫЙ Java-объект.
     * Аналогичный эффект даёт serialization туда-обратно (например, JSON в REST-контроллере).
     */
    @Transactional
    public void demonstrateDetachProblem() {
        Event e = new Event();
        e.setNameEvent("Турнир для detach");
        e.setRingsCount(2);
        Event saved = eventRepository.save(e);
        entityManager.flush();

        entityManager.detach(saved);

        Event reloaded = eventRepository.findEventByEventId(saved.getEventId())
                .orElseThrow(RuntimeException::new);

        log.warn("saved == reloaded           : {}  (false — после detach managed-копия ушла из PC)",
                saved == reloaded);
        log.warn("saved.equals(reloaded)      : {}  (ПРОБЛЕМА: false, хотя в БД одна запись)",
                saved.equals(reloaded));
    }

    /**
     * Пример 4: lazy-связь @ManyToOne(fetch = LAZY).
     * У EventBidFighter поле event помечено LAZY — bid.getEvent() возвращает HibernateProxy.
     *
     * ВАЖНО: прокси создаётся ТОЛЬКО если целевой entity ещё НЕ в persistence context.
     * Если Event уже загружен в PC (например, мы уже делали findById), Hibernate
     * вернёт ту же managed-ссылку, а не прокси (identity map). Поэтому здесь
     * мы тщательно контролируем порядок и явно очищаем PC через entityManager.clear().
     *
     * Spring Boot по умолчанию держит OpenSessionInView → одну сессию на весь HTTP-запрос,
     * поэтому раздельные транзакции тут не спасают: PC общий. Чистим вручную.
     */
    @Transactional
    public void demonstrateLazyAssociationProxyProblem() {
        // 1) Сохраняем event и связанный с ним bid
        Event e = new Event();
        e.setNameEvent("Турнир с lazy");
        e.setRingsCount(2);
        Event savedEvent = eventRepository.save(e);

        EventBidFighter bid = new EventBidFighter();
        bid.setEvent(savedEvent);
        bid.setApproved(1);
        EventBidFighter savedBid = bidRepository.save(bid);

        Integer eventId = savedEvent.getEventId();
        Long bidId = savedBid.getId();

        // 2) Очищаем PC — иначе ниже Hibernate отдал бы те же managed-ссылки
        entityManager.flush();
        entityManager.clear();

        // 3) Грузим bid. Event ещё не в PC → bid.getEvent() возвращает HibernateProxy.
        EventBidFighter loadedBid = bidRepository.findById(bidId)
                .orElseThrow(RuntimeException::new);
        Event lazyEvent = loadedBid.getEvent();

        log.warn("e.getClass(): {}", e.getClass().getName());
        log.warn("e instanceof HibernateProxy: {}", e instanceof HibernateProxy);
        log.warn("lazyEvent.getClass()                  : {}", lazyEvent.getClass().getName());
        log.warn("lazyEvent instanceof HibernateProxy   : {}  (true — прокси lazy-связи)",
                lazyEvent instanceof HibernateProxy);
        log.warn("lazyEvent.getClass() == Event.class   : {}  (false — прокси-класс отличается от Event)",
                lazyEvent.getClass() == Event.class);

        // 4) Принудительно инициализируем прокси (подтянет данные из БД).
        //    Без этого после clear() любое обращение к полю прокси бросило бы LazyInitializationException.
        Hibernate.initialize(lazyEvent);

        // 5) Очищаем PC ещё раз — иначе следующий findEventByEventId вернёт ТОТ ЖЕ прокси
        //    (Hibernate его уже инициализировал и держит в identity map).
        entityManager.clear();

        // 6) Теперь грузим "настоящий" Event напрямую — это обычный класс Event, не прокси.
        Event freshEvent = eventRepository.findEventByEventId(eventId)
                .orElseThrow(RuntimeException::new);

        log.warn("freshEvent.getClass()                 : {}", freshEvent.getClass().getName());
        log.warn("freshEvent instanceof HibernateProxy  : {}  (false — обычный Event)",
                freshEvent instanceof HibernateProxy);

        log.warn("lazyEvent.getClass() == freshEvent.getClass() : {}  (false — proxy vs Event)",
                lazyEvent.getClass() == freshEvent.getClass());

        // Object.equals = сравнение по ссылке → false.
        // С раскомментированным ручным equals в Event.java прокси разворачивается → станет true.
        log.warn("lazyEvent.equals(freshEvent)          : {}  (ПРОБЛЕМА с Object.equals: false, хотя в БД одна запись)",
                lazyEvent.equals(freshEvent));
    }

    /**
     * Пример 5: entityManager.getReference() — единственный обычный случай,
     * когда repository.findById() возвращает ПРОКСИ, а не реальный entity.
     *
     * По умолчанию findById() даёт реальный entity (выполнил SELECT или достал managed-объект из PC).
     * Но JPA-контракт find() говорит: "если managed-объект для этого id уже в PC — вернуть его".
     * Если перед findById сделать getReference (создаёт прокси без SELECT'а и кладёт в PC),
     * то следующий findById для той же записи отдаст ТОТ ЖЕ прокси — это identity map в действии.
     *
     * Сценарий редкий: getReference в обычном коде вызывают нечасто. Чаще прокси на entity
     * попадает в PC через ленивую загрузку связи другого entity. Эффект на findById тот же.
     */
    @Transactional
    public void demonstrateGetReferenceProblem() {
        // 1) Сохраняем event, чтобы запись реально существовала в БД
        Event e = new Event();
        e.setNameEvent("Турнир для getReference");
        e.setRingsCount(2);
        Integer id = eventRepository.save(e).getEventId();
        entityManager.flush();
        entityManager.clear();   // выкидываем managed-копию из PC, начинаем с чистого листа

        // 2) getReference НЕ идёт в БД — создаёт прокси-заглушку и кладёт в PC.
        //    В логах НИКАКОГО SELECT'а на этой строке быть не должно.
        Event ref = entityManager.getReference(Event.class, id);

        log.warn("ref.getClass()                  : {}", ref.getClass().getName());
        log.warn("ref instanceof HibernateProxy   : {}  (true — getReference ВСЕГДА возвращает прокси)",
                ref instanceof HibernateProxy);
        log.warn("ref.getClass() == Event.class   : {}  (false — это сгенерированный подкласс)",
                ref.getClass() == Event.class);

        // 3) Теперь findById. Можно было бы ожидать реальный Event, НО Hibernate видит,
        //    что в identity map уже есть managed-объект с этим id (наш прокси) — и отдаёт ЕГО.
        //    Никакого SELECT'а тут тоже не будет (find проверяет PC первым делом).
        Event found = eventRepository.findById(id).orElseThrow(RuntimeException::new);

        log.warn("found.getClass()                : {}", found.getClass().getName());
        log.warn("found instanceof HibernateProxy : {}  (ИСКЛЮЧЕНИЕ: true, хотя findById обычно даёт real entity)",
                found instanceof HibernateProxy);
        log.warn("found == ref                    : {}  (true — это ОДИН и тот же Java-объект из identity map)",
                found == ref);

        // 4) До этой точки SELECT'а на Event так и не было. Дёрнем поле — прокси инициализируется.
        log.warn("--- сейчас впервые пойдём в БД за данными прокси ---");
        String name = ref.getNameEvent();
        log.warn("ref.getNameEvent()              : {}  (данные подтянулись из БД)", name);

        // 5) После инициализации класс прокси НЕ меняется — это всё ещё прокси-обёртка,
        //    просто её внутреннее состояние теперь заполнено.
        log.warn("после первого обращения к полю:");
        log.warn("  ref instanceof HibernateProxy : {}  (true — класс прокси неизменен)",
                ref instanceof HibernateProxy);
        log.warn("  org.hibernate.Hibernate.isInitialized(ref) : {}  (true — данные на месте)",
                org.hibernate.Hibernate.isInitialized(ref));
    }

    public void getData(int eventId) {
        //Event event = eventRepository.findEventByEventId(eventId).orElseThrow(RuntimeException::new);
        Event event = eventRepository.findEventWithBidsAndFighters(eventId).orElseThrow(RuntimeException::new);
        //Event event = eventRepository.findEventByEventIdGraph(eventId).orElseThrow(RuntimeException::new);
        List<EventBidFighter> bids = event.getEventBidFighters();
        for (EventBidFighter bid : bids) {
            log.info("bid.getFighter().getFirstName(): {}", bid.getFighter().getFirstName());
        }
    }

}
