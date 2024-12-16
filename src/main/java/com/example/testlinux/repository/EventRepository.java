package com.example.testlinux.repository;

import com.example.testlinux.domain.Event;
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
    Optional<Event> findEventByEventId(Integer eventId);

    @Query(value = "SELECT * FROM `events` WHERE event_id = 175", nativeQuery = true)
    Optional<Event> findEventByMySql();
    /*@Query("SELECT new com.strikerstat.webapp.dto.open_events.EventVersionDto(e.version) FROM Event e WHERE e.eventId = :eventId")
    Optional<Event> getEventVersionDto(@Param("eventId") Integer eventId);*/

}