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

import java.util.List;
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
        log.info("lostUpdateWithMyTransaction()  rebase 3 battle.sectionNumber{}; ", battle.getSectionNumber());
        //throw new Exception("пробрасываем исключение Exception"); //- откатывает транзакция
        //TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//  - откатывает транзакция
    }

    @Transactional(readOnly = true)
    public void testTransactionReadOnly() {
        final long battleId = 1L;
        Battle battle = battlesRepository.getOpenEventBattleByBattleId(battleId)
                .orElseThrow(() -> new ValidationException("testTransactionReadOnly error"));
        battle.setSectionNumber(25);// не чего не сохраняет
        //battlesRepository.updateBattle(battleId);// вызывает исключение
    }

    // === ДЕМО пункт 2: readOnly=true vs readOnly=false vs БЕЗ @Transactional ===
    // Каждый метод: findById(1) ДВАЖДЫ (для L1-кеша) + модификация. Смотрите SQL-лог + b1==b2.
    @Transactional(readOnly = true)
    public void multiReadReadOnly() {
        multiReadBody("readOnly=true ");
    }

    @Transactional // readOnly=false по умолчанию
    public void multiReadReadWrite() {
        multiReadBody("readOnly=false");
    }

    @Transactional
    public void newEntityWithoutSave() {
        Battle b = Battle.create(5);        // TRANSIENT — Hibernate её не знает
        battlesRepository.flush();          // ← INSERT НЕ произойдёт (b нет в PC)
        log.info("id после flush = {}", b.getIdBattle());  // null — не сохранилась!
    }

    public void multiReadNoTx() { // вообще без @Transactional
        multiReadBody("без @Transact");
    }

    private void multiReadBody(String label) {
        log.info("=== [{}] START ===", label);
        Battle b1 = battlesRepository.findById(1L).orElseThrow();
        b1.setSectionNumber(b1.getSectionNumber() + 1);
        log.info("[{}] после ПЕРВОГО findById(1), b1.getSectionNumber(): {}", label, b1.getSectionNumber());
        //battlesRepository.flush();
        Battle b2 = battlesRepository.getOpenEventBattleByBattleId(1L).orElseThrow(); // тот же id
        log.info("[{}] после ВТОРОГО findById(1): тот же инстанс (b1==b2)? {}", label, b1 == b2);
        log.info("b1.getSectionNumber(): {}, b2.getSectionNumber(): {}",
                b1.getSectionNumber(), b2.getSectionNumber());
        b1.setSectionNumber(b1.getSectionNumber() + 1);          // модификация (инкремент, чтобы UPDATE точно сработал)
        log.info("=== [{}] END (setSectionNumber выполнен, дальше commit) ===", label);
        throw new ValidationException("rollBack");
    }

    // === ДЕМО save() vs flush(): видно ТОЛЬКО внутри @Transactional (без tx save() коммитит сразу) ===
    // UPDATE: save() ПЛАНИРУЕТ, flush() ВЫПОЛНЯЕТ. Смотрите, ГДЕ в логе появится строка 'update'.
    @Transactional
    public void saveVsFlushUpdate() {
        Battle b = battlesRepository.findById(1L).orElseThrow();   // select
        b.setSectionNumber(b.getSectionNumber() + 1);
        log.info(">>> ПЕРЕД save()");
        battlesRepository.save(b);                                 // UPDATE отложен — 'update' в логе ещё НЕ появится
        log.info(">>> ПОСЛЕ save() — UPDATE в БД ещё НЕ ушёл");
        battlesRepository.flush();                                 // <-- 'update' появится ИМЕННО ЗДЕСЬ
        log.info(">>> ПОСЛЕ flush() — UPDATE только что ушёл в БД (строка 'update' выше)");
    }

    // ИСКЛЮЧЕНИЕ: @GeneratedValue(IDENTITY) → save() новой сущности делает INSERT СРАЗУ (нужен id из БД).
    // ВНИМАНИЕ: добавляет строку в таблицу battles.
    @Transactional
    public void saveInsertIdentity() {
        Battle b = Battle.create(5);
        log.info(">>> ПЕРЕД save() новой сущности (IDENTITY), id пока = {}", b.getIdBattle());
        battlesRepository.save(b);                                 // 'insert' появится ПРЯМО ЗДЕСЬ (не откладывается)
        log.info(">>> ПОСЛЕ save() — INSERT уже ушёл, БД сгенерировала id = {} (flush НЕ нужен)", b.getIdBattle());
    }

    // === Замер производительности readOnly vs read-write на массовой загрузке ===
    // read-write: Hibernate держит snapshot на КАЖДУЮ сущность (для dirty checking) + на коммите
    //             делает проход dirty checking по всем N. Больше памяти и CPU.
    @Transactional(readOnly = false)
    public long perfReadWrite() {
        return loadAndSum("READ-WRITE");
    }

    // readOnly: snapshot'ы НЕ создаются, dirty checking и flush пропускаются. Меньше памяти и CPU.
    @Transactional(readOnly = true)
    public long perfReadOnly() {
        return loadAndSum("READ-ONLY");
    }

    private long loadAndSum(String label) {
        System.gc(); // индикативно: попросить GC до замера (не гарантия, но сглаживает картину)
        long heapBefore = usedHeapMb();
        long t0 = System.nanoTime();

        List<Battle> all = battlesRepository.findAll(); // грузим ВСЕ сущности в persistence context
        long loadMs = (System.nanoTime() - t0) / 1_000_000;

        long sum = 0;                                   // просто читаем, ничего не меняем
        for (Battle b : all) {
            if (b.getSectionNumber() != null) sum += b.getSectionNumber();
        }
        long heapUsedByLoad = usedHeapMb() - heapBefore;

        log.info("[{}] N={}, загрузка+обход={} ms, heap на набор≈{} MB, sum={}",
                label, all.size(), loadMs, heapUsedByLoad, sum);
        return sum;
        // ↑ ПОСЛЕ возврата из метода TransactionInterceptor делает commit:
        //   read-write → проход dirty checking по всем N сущностям; readOnly → пропуск (FlushMode.MANUAL)
    }

    private long usedHeapMb() {
        Runtime r = Runtime.getRuntime();
        return (r.totalMemory() - r.freeMemory()) / 1024 / 1024;
    }
}
