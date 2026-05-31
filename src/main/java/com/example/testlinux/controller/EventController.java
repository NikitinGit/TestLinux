package com.example.testlinux.controller;

import com.example.testlinux.service.EventServiceTest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {

    private final EventServiceTest eventServiceTest;
    @GetMapping
    public ResponseEntity<Void> test() {
        eventServiceTest.getData(175);
        return ResponseEntity.ok().build();
    }
}
