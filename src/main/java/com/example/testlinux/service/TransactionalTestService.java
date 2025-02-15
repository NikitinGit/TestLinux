package com.example.testlinux.service;

import com.example.testlinux.domain.Battle;
import com.example.testlinux.domain.EventBidFighter;
import com.example.testlinux.repository.BattlesRepository;
import com.example.testlinux.repository.EventBidFighterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class TransactionalTestService {

    @Autowired
    private BattlesRepository battlesRepository;

    @Autowired
    private EventBidFighterRepository eventBidFighterRepository;

    private static int count;

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public void updateBattle(Long battleId) {
        Battle battle = battlesRepository.getOpenEventBattleByBattleId(battleId).get();
        battle.setSectionNumber(battle.getSectionNumber() + 1);
        //battlesRepository.save(battle); не обязательно

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
}
