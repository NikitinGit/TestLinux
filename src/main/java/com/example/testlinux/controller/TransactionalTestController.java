package com.example.testlinux.controller;

import com.example.testlinux.service.TransactionalTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/transaction")
public class TransactionalTestController {

    @Autowired
    private TransactionalTestService transactionalTestService;

    @RequestMapping(value = "/read_uncommitted", method = RequestMethod.GET)
    public ResponseEntity<Void> updateBattle(@RequestParam Long battleId) {
        transactionalTestService.updateBattle(battleId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/dirty_read", method = RequestMethod.GET)
    public ResponseEntity<Void> dirtyRead(@RequestParam Long battleId) {
        transactionalTestService.dirtyRead(battleId);
        log.info("dirtyRead() Controller End");
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/read_tables_changed", method = RequestMethod.GET)
    public ResponseEntity<Void> readTablesChanged(@RequestParam Long battleId) {
        transactionalTestService.readTablesChanged(battleId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/change_table_data", method = RequestMethod.GET)
    public ResponseEntity<Void> changeTableData(@RequestParam Long battleId) {
        transactionalTestService.changeData(battleId);
        log.info("changeData() Controller End battleId: {}", battleId);
        return ResponseEntity.ok().build();
    }
}

