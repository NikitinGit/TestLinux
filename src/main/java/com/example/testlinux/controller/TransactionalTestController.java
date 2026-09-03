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

    @RequestMapping(value = "/lost-update", method = RequestMethod.GET)
    public ResponseEntity<Void> lostUpdate() {
        transactionalTestService.lostUpdateWithoutTransaction();
        log.info("lostUpdate() Controller End");
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/lost-update2", method = RequestMethod.GET)
    public ResponseEntity<Void> lostUpdate2() throws Exception {
        transactionalTestService.lostUpdateWithTransaction();
        log.info("lostUpdate2() Controller End");
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/lost-update3", method = RequestMethod.GET)
    public ResponseEntity<Void> lostUpdate3() {
        try {
            transactionalTestService.lostUpdateWithMyTransaction();
        } catch (Exception e) {
            log.info("lostUpdate3() catch (Exception e) ");
            log.info("lostUpdate3() 2 catch (Exception ignored)");
        }
        log.info("lostUpdate3() Controller End");
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/read-only-test", method = RequestMethod.GET)
    public ResponseEntity<Void> testTransactionReadOnly() {
        transactionalTestService.testTransactionReadOnly();
        log.info("testTransactionReadOnly() Controller End");
        return ResponseEntity.ok().build();
    }

    // ДЕМО пункт 2: сравнение readOnly=true / readOnly=false / без @Transactional
    @RequestMapping(value = "/multi-read-ro", method = RequestMethod.GET)
    public ResponseEntity<Void> multiReadReadOnly() {
        transactionalTestService.multiReadReadOnly();
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/multi-read-rw", method = RequestMethod.GET)
    public ResponseEntity<Void> multiReadReadWrite() {
        transactionalTestService.multiReadReadWrite();
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/multi-read-notx", method = RequestMethod.GET)
    public ResponseEntity<Void> multiReadNoTx() {
        transactionalTestService.multiReadNoTx();
        return ResponseEntity.ok().build();
    }

    // ДЕМО save() vs flush() (внутри @Transactional): смотрите, ГДЕ в логе появится 'update'/'insert'
    @RequestMapping(value = "/save-vs-flush", method = RequestMethod.GET)
    public ResponseEntity<Void> saveVsFlushUpdate() {
        transactionalTestService.saveVsFlushUpdate();
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/save-insert-identity", method = RequestMethod.GET)
    public ResponseEntity<Void> saveInsertIdentity() {
        transactionalTestService.saveInsertIdentity();
        return ResponseEntity.ok().build();
    }

    // Таймер вокруг вызова — чтобы захватить и commit (dirty-check проход у read-write)
    @RequestMapping(value = "/perf-read-write", method = RequestMethod.GET)
    public ResponseEntity<Void> perfReadWrite() {
        long t0 = System.nanoTime();
        transactionalTestService.perfReadWrite();
        log.info("[READ-WRITE] ВСЕГО вкл. commit (dirty-check по всем N): {} ms", (System.nanoTime() - t0) / 1_000_000);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/perf-read-only", method = RequestMethod.GET)
    public ResponseEntity<Void> perfReadOnly() {
        long t0 = System.nanoTime();
        transactionalTestService.perfReadOnly();
        log.info("[READ-ONLY ] ВСЕГО, flush пропущен (нет dirty-check): {} ms", (System.nanoTime() - t0) / 1_000_000);
        return ResponseEntity.ok().build();
    }
}

