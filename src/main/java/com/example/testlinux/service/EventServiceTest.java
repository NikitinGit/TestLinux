package com.example.testlinux.service;

import com.example.testlinux.domain.Event;
import com.example.testlinux.domain.EventBidFighter;
import com.example.testlinux.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceTest {

    private final EventRepository eventRepository;

    public void getData(int eventId) {
        Event event = eventRepository.findEventByEventId(eventId)
                .orElseThrow(RuntimeException::new);
        List<EventBidFighter> bids = event.getEventBidFighters();
        for (EventBidFighter bid : bids) {
            log.info("bid.getFighter().getFirstName(): {}", bid.getFighter().getFirstName());
        }
    }

}
