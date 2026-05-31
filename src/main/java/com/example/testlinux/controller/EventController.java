package com.example.testlinux.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/event")
public class EventController {

    @GetMapping
    public ResponseEntity<Void> test() {

        return ResponseEntity.ok().build();
    }
}
