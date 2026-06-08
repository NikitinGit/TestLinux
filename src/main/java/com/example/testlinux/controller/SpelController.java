package com.example.testlinux.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpelController {

    @RequestMapping(value = "/event/test1", method = RequestMethod.GET)
    public ResponseEntity<Void> test1() {
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/event/test2", method = RequestMethod.GET)
    public ResponseEntity<Void> test2() {
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/event/test3", method = RequestMethod.GET)
    public ResponseEntity<Void> test3() {
        return ResponseEntity.ok().build();
    }
}
