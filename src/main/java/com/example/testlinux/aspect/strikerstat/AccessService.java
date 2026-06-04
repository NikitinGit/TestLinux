package com.example.testlinux.aspect.strikerstat;


import com.example.testlinux.repository.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AccessService {

    @Autowired
    EventRepository eventRepository;

    public boolean doesEventBelongToOrganizer(Integer organizerLoginId, Integer eventId){
        return eventRepository.doesEventBelongToOrganizer(organizerLoginId, eventId);
    }
}