package com.example.testlinux.controller;

import com.example.testlinux.service.TransactionalTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
public class TransactionalTestController {

    @Autowired
    private TransactionalTestService transactionalTestService;

    // Запуск метода для обновления данных
    @RequestMapping(value = "/read_uncommitted", method = RequestMethod.GET)
    public ResponseEntity<Void> updateBattle(@RequestParam Long battleId) {
        transactionalTestService.updateBattle(battleId); // Этот запрос будет менять данные
        return ResponseEntity.ok().build();
    }

    // Запуск метода для грязного чтения
    @RequestMapping(value = "/dirty_read", method = RequestMethod.GET)
    public ResponseEntity<Void> dirtyRead(@RequestParam Long battleId) {
        transactionalTestService.dirtyRead(battleId); // Этот запрос будет читать данные
        return ResponseEntity.ok().build();
    }
}

