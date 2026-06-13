package com.example.testlinux.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/test")
public class AspectController {

    @GetMapping("/1")
    public ResponseEntity<String> endPointN1() {
        return ResponseEntity.ok("AspectController endPointN1");
    }

    @GetMapping("/2")
    public ResponseEntity<String> endPointN2() {
        return ResponseEntity.badRequest().body("AspectController endPointN2");
    }

    @GetMapping("/3")
    public ResponseEntity<String> endPointN3() {
        return ResponseEntity.unprocessableEntity().body("AspectController endPointN3");
    }
}
