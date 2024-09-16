package com.example.testlinux.repository;

import com.example.testlinux.domain.Event;
import com.example.testlinux.domain.EventBidFighter;
import com.example.testlinux.domain.Fighter;
import com.example.testlinux.dto.FighterDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;


public interface EventBidFighterRepository extends JpaRepository<EventBidFighter, Long> {
    @Query("SELECT t FROM EventBidFighter t WHERE t.eventId = :eventId")
    //@Query(value = "SELECT f.id AS fighter_id, f.first_name AS fighter_name, ebf.id AS event_bid_fighter_id, ebf.approved AS bid_approved FROM events_bids_fighters ebf INNER JOIN fighters f ON ebf.fighter_id = f.id WHERE ebf.idEvent = 122", nativeQuery = true)
    //@Query(value = "SELECT ebf.id AS ebidid, ebf.idEvent, ebf.fighter_id, ebf.approve_weight, ebf.level_by_site, f.* FROM events_bids_fighters ebf INNER JOIN fighters f ON ebf.fighter_id = f.id WHERE ebf.idEvent = :eventId", nativeQuery = true)
    List<EventBidFighter> getFightersFromBids(@Param("eventId") Integer eventId);

    @Query("SELECT new com.example.testlinux.dto.FighterDto(fgt.firstName, fgt.phoneByFighter) " +
            "FROM EventBidFighter ebf " +
            "INNER JOIN Fighter fgt ON ebf.fighter.id = fgt.id " +
            "WHERE ebf.eventId = :eventId")
    List<FighterDto> getAllFightersByEventId(@Param("eventId") Integer eventId);
}

