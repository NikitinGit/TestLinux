package com.example.testlinux.service;

import com.example.testlinux.aspect.acpectonclass.TransactionalRollbackAll;
import com.example.testlinux.domain.Battle;
import com.example.testlinux.domain.EventBidFighter;
import com.example.testlinux.exceptions.ValidationException;
import com.example.testlinux.repository.BattlesRepository;
import com.example.testlinux.repository.EventBidFighterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Slf4j
@Service
public class TransactionalTestService {

    @Autowired
    private BattlesRepository battlesRepository;

    @Autowired
    private EventBidFighterRepository eventBidFighterRepository;

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public void updateBattle(Long battleId) {
        Battle battle = battlesRepository.getOpenEventBattleByBattleId(battleId).get();
        battlesRepository.updateBattle(battleId);
        //battle.setSectionNumber(battle.getSectionNumber() + 1);
        //battlesRepository.save(battle); не обязательно  battlesRepository.flush();

        log.info("updateBattleBgn() - ThreadName: {}, battle1.getSectionNumber(): {}",
                Thread.currentThread().getName(), battle.getSectionNumber());

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Battle battle2 = battlesRepository.getOpenEventBattleByBattleId(battleId).get();
        log.info("updateBattleEnd() - ThreadName: {}, battle2.getSectionNumber(): {}",
                Thread.currentThread().getName(), battle2.getSectionNumber());
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public void dirtyRead(Long battleId) {
        Battle battle = battlesRepository.getOpenEventBattleByBattleId(battleId).get();//battle.setSectionNumber(25);
        battlesRepository.updateBattle(battleId);
        log.info("dirtyRead() - Read sectionNumber: {}, ThreadName: {}",
                battle.getSectionNumber(), Thread.currentThread().getName());
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public void readTablesChanged(Long battleId) {
        Battle battle = battlesRepository.getOpenEventBattleByBattleId(battleId).get();
        battle.setSectionNumber(battle.getSectionNumber() + 1);

        log.info("readTablesChangedBgn() - ThreadName: {}, battle1.getSectionNumber(): {}",
                Thread.currentThread().getName(), battle.getSectionNumber());

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        EventBidFighter eventBidFighter = eventBidFighterRepository.getBid().get();
        eventBidFighter.setApproved(eventBidFighter.getApproved() + 1);
        log.info("readTablesChangedEnd() - ThreadName: {}, eventBidFighter.getApproved(): {}",
                Thread.currentThread().getName(), eventBidFighter.getApproved());
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public void changeData(Long battleId) {
        Battle battle = battlesRepository.getOpenEventBattleByBattleId(battleId).get();
        battle.setSectionNumber(1);
        if (battle.getSectionNumber().equals(1)) {
            EventBidFighter eventBidFighter = eventBidFighterRepository.getBid().get();
            eventBidFighter.setApproved(0);
            log.info("changeData() - ThreadName: {}, eventBidFighter.getApproved(): {}",
                    Thread.currentThread().getName(), eventBidFighter.getApproved());
        }
    }

    public void lostUpdateWithoutTransaction() {
        final long battleId = 1L;
        Battle battle = battlesRepository.getOpenEventBattleByBattleId(battleId)
                .orElseThrow(() -> new ValidationException("lostUpdate error"));
        log.info("lostUpdateWithoutTransaction() battle.sectionNumber{}; ", battle.getSectionNumber());

        try {
            Thread.sleep(2555L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //battlesRepository.removeByIdBattle(1L);
        battle.setSectionNumber(battle.getSectionNumber() + 1);
        battlesRepository.save(battle);
        //battlesRepository.flush();// не откатывается
        throw new ValidationException("error lostUpdateWithoutTransaction()");
    }

    @Transactional(rollbackFor = Exception.class)
    public void lostUpdateWithTransaction() throws Exception {
        final long battleId = 2L;
        Battle battle = battlesRepository.getOpenEventBattleByBattleId(battleId)
                .orElseThrow(() -> new ValidationException("lostUpdate error"));
        log.info("lostUpdateWithTransaction() battle.sectionNumber{}; ", battle.getSectionNumber());

        try {
            Thread.sleep(2555L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        battlesRepository.updateBattle(battleId);
        //battlesRepository.flush(); - откатывается если до конца метода не дойдет выполнение
        //throw new ValidationException("error"); //- RuntimeException (unchecked) → транзакция ОТКАТЫВАЕТСЯ

        // checked-исключение (наследник Exception, НЕ RuntimeException): Spring по умолчанию
        // НЕ откатывает на него транзакцию → updateBattle останется закоммиченным в БД.
        // (чтобы всё-таки откатывалось, нужно @Transactional(rollbackFor = Exception.class))
        try {
            log.info("lostUpdateWithTransaction() try, sectionNumber={}", battle.getSectionNumber());
            throw new RuntimeException("ошибка внутри try — БЕЗ catch она откатила бы транзакцию");
        } catch (Exception e) {
            log.warn("поймали и проглотили: {} → транзакция НЕ откатится:", e.getMessage());
            //throw new Exception("пробрасываем исключение Exception"); - откатывает транзакция
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//  - откатывает транзакция
        }
        //throw new Exception("checked exception -> транзакция НЕ откатывается, updateBattle сохранён");
    }

    @TransactionalRollbackAll()
    public void lostUpdateWithMyTransaction() {
        final long battleId = 2L;
        Battle battle = battlesRepository.getOpenEventBattleByBattleId(battleId)
                .orElseThrow(() -> new ValidationException("lostUpdate error"));
        log.info("lostUpdateWithMyTransaction() battle.sectionNumber{}; ", battle.getSectionNumber());
        battlesRepository.updateBattle(battleId);
        log.info("lostUpdateWithMyTransaction() rebase 2 battle.sectionNumber{}; ", battle.getSectionNumber());
        //throw new Exception("пробрасываем исключение Exception"); //- откатывает транзакция
        //TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//  - откатывает транзакция
    }
}
