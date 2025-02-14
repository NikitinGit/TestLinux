package com.example.testlinux.service;

import com.example.testlinux.domain.Battle;
import com.example.testlinux.repository.BattlesRepository;
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

    private static int count;

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public void updateBattle(Long battleId) {
        log.info("updateBattle() method, current transaction: {}",
                TransactionAspectSupport.currentTransactionStatus().getTransactionName());
        Battle battle = battlesRepository.getOpenEventBattleByBattleId(battleId).get();

        if (++count % 2 == 1) {
            battle.setSectionNumber(battle.getSectionNumber() + 1);
            log.info("updateBattleCHN() - ThreadName: {}, battle1.getSectionNumber(): {}",
                    Thread.currentThread().getName(), battle.getSectionNumber());
        }

        battlesRepository.save(battle);
        battlesRepository.flush();

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


    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public void dirtyRead(Long battleId) {
        log.info("dirtyRead() method, current transaction: {}",
                TransactionAspectSupport.currentTransactionStatus().getTransactionName());
        Battle battle = battlesRepository.getOpenEventBattleByBattleId(battleId).get();

        log.info("dirtyRead() - Read sectionNumber: {}, ThreadName: {}",
                battle.getSectionNumber(), Thread.currentThread().getName());
    }
}
