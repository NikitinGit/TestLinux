package com.example.testlinux.controller;

import com.example.testlinux.aspect.CheckOrganizerAccess;
import com.example.testlinux.service.EventServiceTest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {

    private final EventServiceTest eventServiceTest;

    @GetMapping
    public ResponseEntity<Void> test() {
        //eventServiceTest.getData(175);
        //eventServiceTest.demonstrateEqualsHashCodeProblem();
        //eventServiceTest.demonstrateLazyAssociationProxyProblem();
        eventServiceTest.demonstrateGetReferenceProblem();
        return ResponseEntity.ok().build();
    }

    //@CheckOrganizerAccess(eventIdFieldName = "eventId")
    @CheckOrganizerAccess(eventIdParamIndex = 0)
    @RequestMapping(value = "/aspect", method = RequestMethod.GET)
    public ResponseEntity<Void> aspect(@RequestParam("eventId") Integer eventId) {
        eventServiceTest.setEventId(eventId);
        return ResponseEntity.ok().build();
    }

    @CheckOrganizerAccess
    @RequestMapping(value = "/aspect2", method = RequestMethod.GET)
    public ResponseEntity<Void> aspect2(@RequestParam("eventId") Integer eventId) {
        eventServiceTest.setEventId(eventId * 2);
        return ResponseEntity.ok().build();
    }
}
