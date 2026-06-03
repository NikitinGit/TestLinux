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
     * DISTINCT нужен, чтобы из-за JOIN с коллекцией не было дубликатов root-сущности Event.
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

    /*@Query("SELECT new com.strikerstat.webapp.dto.open_events.EventVersionDto(e.version) FROM Event e WHERE e.eventId = :eventId")
    Optional<Event> getEventVersionDto(@Param("eventId") Integer eventId);*/

}