package com.example.testlinux.repository;

import com.example.testlinux.domain.Event;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository("eventRepository")
public interface EventRepository extends JpaRepository<Event, Integer> {
    //Optional<Event> findEventByEventId(Integer eventId);

    @Query(value = "SELECT * FROM `events` WHERE event_id = :eventId", nativeQuery = true)
    Optional<Event> findEventByEventId(@Param("eventId")  Integer eventId);

    /**
     * Решение N+1 №1 — JPQL с JOIN FETCH.
     * Один SQL: events LEFT JOIN events_bids_fighters LEFT JOIN fighters.
     * NB: с Hibernate 6 DISTINCT для дедупликации корня НЕ нужен — дубли Event убираются
     * автоматически (в Hibernate 5 был обязателен). Здесь DISTINCT оставлен как legacy; в H6 он лишь
     * добавляет бесполезный SQL DISTINCT — можно убрать.
     */
    @Query("SELECT DISTINCT e FROM Event e " +
            "LEFT JOIN FETCH e.eventBidFighters bf " +
            "LEFT JOIN FETCH bf.fighter " +
            "WHERE e.eventId = :eventId")
    Optional<Event> findEventWithBidsAndFighters(@Param("eventId") Integer eventId);

    /**
     * Решение N+1 №2 — декларативно через @EntityGraph.
     * Spring Data сам строит JOIN FETCH по перечисленным путям, JPQL остаётся простым.
     */
    @EntityGraph(attributePaths = {"eventBidFighters", "eventBidFighters.fighter"})
    @Query("SELECT e FROM Event e WHERE e.eventId = :eventId")
    Optional<Event> findEventByEventIdGraph(@Param("eventId") Integer eventId);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Event t " +
            "WHERE t.eventId = :eventId AND t.organizerLogin = :organizerLoginId ")
    boolean doesEventBelongToOrganizer(@Param("organizerLoginId") Integer organizerLoginId, @Param("eventId") Integer eventId);

    // === ДЕМО: LEFT JOIN vs LEFT JOIN FETCH (отличается ТОЛЬКО словом FETCH) ===
    // (DISTINCT ниже — legacy: с Hibernate 6 он для дедупликации корня НЕ нужен, дубли убираются сами)

    // Обычный LEFT JOIN: коллекция eventBidFighters остаётся LAZY → доступ к ней = ОТДЕЛЬНЫЙ SELECT.
    @Query("SELECT DISTINCT e FROM Event e LEFT JOIN e.eventBidFighters WHERE e.eventId = :id")
    Optional<Event> findEventPlainJoin(@Param("id") Integer id);

    @Query("SELECT e FROM Event e WHERE e.eventId = :id AND EXISTS " +
            "(SELECT 5 FROM EventBidFighter bf WHERE bf.event = e AND bf.approved = 1)")
    List<Event> findEventsWithApprovedBids(@Param("id") Integer id);

    // LEFT JOIN FETCH: коллекция грузится ТЕМ ЖЕ запросом → доступ к ней доп. SELECT НЕ требует.
    @Query("SELECT DISTINCT e FROM Event e LEFT JOIN FETCH e.eventBidFighters WHERE e.eventId = :id")
    Optional<Event> findEventFetchJoin(@Param("id") Integer id);

    // === ДЕМО дублей корня: одинаковый JOIN FETCH БЕЗ и С DISTINCT (оба возвращают List<Event>) ===
    // ВАЖНО (Hibernate 6): дубли корня Event убираются АВТОМАТИЧЕСКИ → ОБА варианта дают list.size()=1.
    // В Hibernate 5 без DISTINCT было бы N дублей (по строке на ребёнка) → distinct был ОБЯЗАТЕЛЕН.
    // С H6 distinct для дедупа НЕ нужен; он лишь добавляет 'select distinct' в SQL (видно в логе).
    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.eventBidFighters WHERE e.eventId = :id")
    List<Event> findEventFetchJoinNoDistinct(@Param("id") Integer id);

    // С DISTINCT: в H6 результат тот же (list.size()=1), но в SQL уходит бесполезный 'select distinct'
    @Query("SELECT DISTINCT e FROM Event e LEFT JOIN FETCH e.eventBidFighters WHERE e.eventId = :id")
    List<Event> findEventFetchJoinDistinct(@Param("id") Integer id);

    /*@Query("SELECT new com.strikerstat.webapp.dto.open_events.EventVersionDto(e.version) FROM Event e WHERE e.eventId = :eventId")
    Optional<Event> getEventVersionDto(@Param("eventId") Integer eventId);*/

}